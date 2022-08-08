package com.deadline826.bedi.security;

import com.deadline826.bedi.login.Domain.Dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Slf4j
public class CustomAuthenticationFilter {
    //SecurityConfig 의 authenticationManagerBean 에서 주입받는다
    private  AuthenticationManager authenticationManager;


    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public Authentication attemptAuthentication(UserDto userDto) throws AuthenticationException {
        try {
            Long id = userDto.getId(); // 카카오가 넘겨주는 랜덤 값
            String password = userDto.getPassword();

            //id, password 로 토큰생성
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(id, password);
            System.out.println("token = " + token);

            //CustomAuthProvider 의 authenticate 에서 토큰을 검사
            Authentication authenticate = authenticationManager.authenticate(token);
            System.out.println("authenticate = " + authenticate);

            //여기까지 성공하면 KakaoService의 login 으로 값 리턴
            return authenticationManager.authenticate(token);
        } catch (Exception e) {
            log.error(String.valueOf(e));
            return null;
        }
    }



}
