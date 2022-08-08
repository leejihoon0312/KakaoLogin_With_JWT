package com.deadline826.bedi.Goal.Service;

import com.deadline826.bedi.Goal.Domain.Dto.GoalPostDto;
import com.deadline826.bedi.Goal.Domain.Goal;
import com.deadline826.bedi.Goal.repository.GoalRepository;
import com.deadline826.bedi.exception.ErrorResponse;
import com.deadline826.bedi.login.Domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@Transactional
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService{
    private final GoalRepository goalRepository;

    @Override
    public boolean checkDateAndDistance(boolean isValidDate , boolean isValidDistance,boolean isEditable,
                                     Goal goal,GoalPostDto goalPostDto,User user) {

        //  거리, 날짜가 유효하다면
        if (isValidDistance && isValidDate && isEditable ){

            goal.setDate(goalPostDto.getDate());
            goal.setTitle(goalPostDto.getTitle());
            goal.setUser(user);
            goal.setX_coordinate(goalPostDto.getArrive_x_coordinate());
            goal.setY_coordinate(goalPostDto.getArrive_y_coordinate());

            saveGoal(goal);
            return true;

        }
        else {
            return false;
        }
    }


    @Override
    public Goal findGoalById(Long id){
        Optional<Goal> findGoal = goalRepository.findById(id);
        return findGoal.get();
    }

    @Override
    public boolean isValidDistance(GoalPostDto goalPostDto){

        Double meter = distance(goalPostDto, "meter");

        if(meter >=20 ){
            return true;

        }
        else {
            return false;
        }
    }

    @Override
    public boolean isValidDate(LocalDate date){
        if(date.compareTo(LocalDate.now()) >=0 ){
            return true;

        }
        else {
            return false;
        }
    }



    @Override
    public List<Goal> getTodayGoals(User user, LocalDate date){
        List<Goal> goalsOrderByTitleAsc = goalRepository.findByUserAndDateOrderByTitleAsc(user,date);
        return goalsOrderByTitleAsc;
    }


    @Override
    public void saveGoal(Goal goal){
        goalRepository.save(goal);
    }

    @Override
    public void removeGoal(Goal goal){ goalRepository.delete(goal); }



    // 두 지점간의 거리 계산    Latitude = Y , Longitude = X
    @Override
    public Double distance(GoalPostDto goalPostDto, String unit) {

        Double lon1 = goalPostDto.getArrive_x_coordinate();
        Double lat1 = goalPostDto.getArrive_y_coordinate();

        Double lon2 = goalPostDto.getStart_x_coordinate();
        Double lat2 = goalPostDto.getStart_y_coordinate();

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if(unit == "meter"){
            dist = dist * 1609.344;
        }

        return (dist);
    }


    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}
