package com.deadline826.bedi.Goal.Domain.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GoalPostDto {

    private LocalDate date;  // "2022-xx-xx" 형식으로 받기
    private String title;

    private Double arrive_x_coordinate;     // 도착지 x 좌표
    private Double arrive_y_coordinate;     // 도착지 y 좌표

    private Double start_x_coordinate;      // 출발지 x 좌표
    private Double start_y_coordinate;      // 출발지 y 좌표

}
