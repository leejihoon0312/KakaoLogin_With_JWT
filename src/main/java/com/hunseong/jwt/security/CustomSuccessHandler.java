package com.hunseong.jwt.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hunseong.jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.hunseong.jwt.security.JwtConstants.*;
import static com.hunseong.jwt.security.JwtConstants.RT_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author : Hunseong-Park
 * @date : 2022-07-04
 */
@RequiredArgsConstructor
@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();


        //토큰 생성
        String accessToken = JWT.create()
                .withSubject(user.getUsername())  // sub = 유저네임( = 아이디(email) )
                .withExpiresAt(new Date(System.currentTimeMillis() + AT_EXP_TIME))  // 토큰 만료시간
                .withClaim("nickName", request.getAttribute("nickname").toString())   //사용자 이름
//                .withClaim("password",request.getAttribute("password").toString())
//                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withIssuedAt(new Date(System.currentTimeMillis()))  // 토큰 생성시간
                .sign(Algorithm.HMAC256(JWT_SECRET));  //JWT_SECRET 키로 암호화
        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withClaim("nickName", request.getAttribute("nickname").toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + RT_EXP_TIME))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .sign(Algorithm.HMAC256(JWT_SECRET));

        // Refresh Token DB에 저장
        userService.updateRefreshToken(user.getUsername(), refreshToken);

        // Access Token , Refresh Token 프론트에 Response Header로 전달
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setHeader(AT_HEADER, accessToken);
        response.setHeader(RT_HEADER, refreshToken);


        //토큰정보 담아서
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put(AT_HEADER, accessToken);
        responseMap.put(RT_HEADER, refreshToken);

        //화면에 보여주기
        new ObjectMapper().writeValue(response.getWriter(), responseMap);
    }
}
