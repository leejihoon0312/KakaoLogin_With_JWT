package com.hunseong.jwt.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author : Hunseong-Park
 * @date : 2022-07-04
 */
@RequiredArgsConstructor
@Component
public class CustomAuthProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();


        System.out.println("CustomAuthProvider username = " + username);
        System.out.println("CustomAuthProvider password = " + password);

        //UserServiceImpl 의 loadUserByUsername 으로 이동
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        String userDetails_password = userDetails.getPassword();
        System.out.println("CustomAuthProvider userDetails_password = " + userDetails_password);

        // PW 검사,  UUID 와 인코딩 된 UUID를 디코딩하여 일치하는지 검사
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Provider - authenticate() : 비밀번호가 일치하지 않습니다.");
        }

//        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());  변경 전

        // Collections.EMPTY_LIST 가 아닌 비밀번호로 인증 함
        //return new UsernamePasswordAuthenticationToken(userDetails, password or userDetails.getPassword() 중 하나 -잘 모르겠음-);

        // Collections.EMPTY_LIST 를 통해 비밀번호 없이 인증가능하게 함
        //CustomAuthenticationFilter 의 attemptAuthentication 으로 복귀
        return new UsernamePasswordAuthenticationToken(userDetails, null, Collections.EMPTY_LIST);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
