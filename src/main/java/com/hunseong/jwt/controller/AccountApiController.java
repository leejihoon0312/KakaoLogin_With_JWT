package com.hunseong.jwt.controller;

import com.hunseong.jwt.domain.dto.UsertDto;
//import com.hunseong.jwt.domain.dto.RoleToUserRequestDto;
import com.hunseong.jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

import static com.hunseong.jwt.security.JwtConstants.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author : Hunseong-Park
 * @date : 2022-07-04
 */
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class AccountApiController {

    private final UserService userService;


//
//    @PostMapping("/signup")
//    public String signup(@RequestBody UsertDto dto) {
//
//        userService.saveUser(dto);
//        return "회원가입완료";
//    }

//    @PostMapping("/role")
//    public ResponseEntity<Long> saveRole(@RequestBody String roleName) {
//        return ResponseEntity.ok(accountService.saveRole(roleName));
//    }

//    @PostMapping("/userrole")
//    public ResponseEntity<Long> addRoleToUser(@RequestBody RoleToUserRequestDto dto) {
//        return ResponseEntity.ok(accountService.addRoleToUser(dto));
//    }

//    @PostMapping("/login")
//    public UserDetails login(@RequestBody AccountRequestDto dto) {
//        System.out.println("1");
//        UserDetails userDetails = accountService.loadUserByUsername(dto.getUsername());
//        System.out.println("2");
//        return userDetails;
//    }

//    @GetMapping("/my")
//    public String my(HttpServletRequest request) {
//        String token = userService.resolveToken(request);
//        return userService.getUserEmail(token);
//
//
//    }

//    @GetMapping("/admin")
//    public ResponseEntity<String> admin() {
//        return ResponseEntity.ok("Admin");
//    }

//    @GetMapping("/refresh")
//    public ResponseEntity<Map<String, String>> refresh(HttpServletRequest request, HttpServletResponse response) {
//        String authorizationHeader = request.getHeader(AUTHORIZATION);
//
//
//        if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_HEADER_PREFIX)) {
//            throw new RuntimeException("JWT Token이 존재하지 않습니다.");
//        }
//        String refreshToken = authorizationHeader.substring(TOKEN_HEADER_PREFIX.length());
//        Map<String, String> tokens = userService.refresh(refreshToken);
//        response.setHeader(AT_HEADER, tokens.get(AT_HEADER));
//        if (tokens.get(RT_HEADER) != null) {
//            response.setHeader(RT_HEADER, tokens.get(RT_HEADER));
//        }
//        return ResponseEntity.ok(tokens);
//    }
}
