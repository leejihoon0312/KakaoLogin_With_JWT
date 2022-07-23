package com.hunseong.jwt.service;

import com.hunseong.jwt.domain.dto.UsertDto;
//import com.hunseong.jwt.domain.dto.RoleToUserRequestDto;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author : Hunseong-Park
 * @date : 2022-07-04
 */
public interface UserService {
    void saveUser(UsertDto dto);   //유저정보 저장
//    Long saveRole(String roleName);
//    Long addRoleToUser(RoleToUserRequestDto dto);

    void updateRefreshToken(String username, String refreshToken);  //RefreshToken 업데이트
//    UserDetails loadUserByUsername(String username);

    Map<String, String> refresh(String refreshToken);  //RefreshToken 으로 AccessToken 받아올때 사용



    String resolveToken(HttpServletRequest request);   // Request의 Header에서 token 값을 가져옵니다.
    String getUserEmail(String token);  //토큰에서 회원 아이디(= 이메일) 추출
}
