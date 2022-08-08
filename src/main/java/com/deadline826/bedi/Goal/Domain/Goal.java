package com.deadline826.bedi.Goal.Domain;

import com.deadline826.bedi.login.Domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Setter
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private Double x_coordinate;   // x 좌표값
    private Double y_coordinate;   // y 좌표값

    private String title;          // 제목

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;             // 연관관게 주인
}
