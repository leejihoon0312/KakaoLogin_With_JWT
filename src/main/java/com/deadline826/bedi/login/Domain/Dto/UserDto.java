package com.deadline826.bedi.login.Domain.Dto;

import com.deadline826.bedi.login.Domain.Authority;
import com.deadline826.bedi.login.Domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class UserDto {
    private Long id;     // 카카오가 넘겨주는 랜덤 값
    private String username;    // 사용자 이름
    private String password;
    private String email;    //카카오 로그인 이메일

    private Authority authority;   //구글 또는 카카오

    public User toEntity() {
        return User.builder()
                .id(id)
                .username(username)
                .email(email)
                .password(password)
                .authority(authority)
                .build();
    }

    public void encodePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
