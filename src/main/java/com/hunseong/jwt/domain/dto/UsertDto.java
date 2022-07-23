package com.hunseong.jwt.domain.dto;

import com.hunseong.jwt.domain.User;
//import com.hunseong.jwt.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author : Hunseong-Park
 * @date : 2022-07-04
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class UsertDto {
    private String username;
    private String password;
    private String nickname;

    public User toEntity() {
        return User.builder()
                .username(username)
                .nickname(nickname)
                .password(password)
                .build();
    }

    public void encodePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
