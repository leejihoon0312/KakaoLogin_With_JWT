package com.deadline826.bedi.security;

import com.deadline826.bedi.login.Domain.User;
import com.deadline826.bedi.login.repository.UserRepository;
import com.deadline826.bedi.login.Service.UserService;
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
import java.util.Optional;


@RequiredArgsConstructor
@Component
public class CustomAuthProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Long id = (Long)authentication.getPrincipal();    // 카카오가 넘겨주는 랜덤 값 (id)

        //UserServiceImpl 의 loadUserById 로 이동  - 기존 loadUserByUsername -> loadUserByUserId 로 수정 -
        UserDetails userDetails = userService.loadUserById(id);
        System.out.println("userDetails = " + userDetails);
        Optional<User> byId = userRepository.findById(id);
        String password = byId.get().getEmail();

        // PW 검사,  UUID 와 인코딩 된 UUID를 디코딩하여 일치하는지 검사
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Provider - authenticate() : 비밀번호가 일치하지 않습니다.");
        }

        // Collections.EMPTY_LIST 를 통해 비밀번호 없이 인증가능하게 함
        //CustomAuthenticationFilter 의 attemptAuthentication 으로 복귀
        return new UsernamePasswordAuthenticationToken(userDetails, null, Collections.EMPTY_LIST);

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}