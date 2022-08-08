package com.deadline826.bedi.Goal.Service;

import com.deadline826.bedi.Goal.Domain.Dto.GoalPostDto;
import com.deadline826.bedi.Goal.Domain.Goal;
import com.deadline826.bedi.login.Domain.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface GoalService {

    List<Goal> getTodayGoals(User user, LocalDate date); // 오늘날짜의 목표 불러오기

    Double distance(GoalPostDto goalPostDto, String unit); //거리계산

    void saveGoal(Goal goal); //목표 저장

    boolean isValidDate(LocalDate date);  //날짜검사

    boolean isValidDistance(GoalPostDto goalPostDto);  //거리검사 (임시)

    Goal findGoalById(Long id);  //아이디로 목표 찾기

    // 날짜와 거리가 유효한지 체크한다
    boolean checkDateAndDistance(boolean isValidDate , boolean isValidDistance, boolean isEditable,
                         Goal goal, GoalPostDto goalPostDto, User user);

    void removeGoal(Goal goal);  //목표 삭제
 }
