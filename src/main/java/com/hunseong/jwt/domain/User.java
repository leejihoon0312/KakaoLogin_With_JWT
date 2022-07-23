package com.hunseong.jwt.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hunseong-Park
 * @date : 2022-07-04
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String nickname;

//    @ManyToMany
//    private final List<Role> roles = new ArrayList<>();

    private String refreshToken;

    public void updateRefreshToken(String newToken) {
        this.refreshToken = newToken;
    }
}
