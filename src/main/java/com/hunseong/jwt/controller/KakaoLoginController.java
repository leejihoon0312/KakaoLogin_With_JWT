package com.hunseong.jwt.controller;


import com.hunseong.jwt.domain.dto.UsertDto;
import com.hunseong.jwt.service.UserService;
import com.hunseong.jwt.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.hunseong.jwt.security.JwtConstants.*;
import static com.hunseong.jwt.security.JwtConstants.RT_HEADER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kakao")
public class KakaoLoginController {

    private final KakaoService kakaoService;
    private final UserService userService;


    @GetMapping(value = "/login" )
    public void returnco(@RequestParam String code , HttpServletResponse response,
                                      HttpServletRequest request) throws URISyntaxException, ServletException, IOException {
        //카카오 서버로 부터 사용자 정보 받아오기
        String accessToken = kakaoService.getAccessToken(code);
        HashMap<String, Object> userInfo = kakaoService.getUserInfo(accessToken);

        //사용자 정보 전달객체 생성
        UsertDto usertDto = new UsertDto();
        usertDto.setUsername((String) userInfo.get("email"));  // username은 아이디로 쓰이기 때문에 email 넣기
        String password = UUID.randomUUID().toString();        // password 랜덤생성
//        String password = "1234";
        usertDto.setPassword(password);
        usertDto.setNickname((String) userInfo.get("nickname"));   // nickname=사용자이름


        // 로그인시 값 전달을 위해 request에 저장 해놓음
        request.setAttribute("username", (String) userInfo.get("email"));
        request.setAttribute("password", password);
        request.setAttribute("nickname", (String) userInfo.get("nickname"));


        //유저정보 저장
        userService.saveUser(usertDto);

        // 카카오로 로그인 해서 JWT 받아오기
        kakaoService.login(request, response);


    }


    //refreshToken 을 이용하여 accessToken 가져오기
    @GetMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);


        if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_HEADER_PREFIX)) {
            throw new RuntimeException("JWT Token이 존재하지 않습니다.");
        }
        String refreshToken = authorizationHeader.substring(TOKEN_HEADER_PREFIX.length());
        Map<String, String> tokens = userService.refresh(refreshToken);  //refreshToken 을 넣으면 accessToken 이 반환 됨
        response.setHeader(AT_HEADER, tokens.get(AT_HEADER));   //위의 accessToken 을 헤더에 넣고
        if (tokens.get(RT_HEADER) != null) {              // refreshToken 의 만료기간이 다가와서 새로 받은 refreshToken 이 있다면
            response.setHeader(RT_HEADER, tokens.get(RT_HEADER));  // refreshToken 도 같이 헤더에 넣는다.
        }
        return ResponseEntity.ok(tokens);   //화면에 출력
    }


    @GetMapping("/my")
    public String my(HttpServletRequest request) {
        String token = userService.resolveToken(request);
        return userService.getUserEmail(token);


    }

}
