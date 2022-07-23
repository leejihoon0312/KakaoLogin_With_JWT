package com.hunseong.jwt.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : Hunseong-Park
 * @date : 2022-07-04
 */
@Slf4j

//@Component
//@AllArgsConstructor
//@RequiredArgsConstructor

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    //SecurityConfig 의 authenticationManagerBean 에서 주입받는다
    private  AuthenticationManager authenticationManager;



//    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
//        super.setAuthenticationManager(authenticationManager);
//    }


    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        String username = request.getParameter("username");  원래 값
//        String password = request.getParameter("password");  원래 값

//        String username = request.getParameter("email");
//        String password = request.getParameter("password");


        System.out.println(" CustomAuthenticationFilter username = " + request.getAttribute("username").toString());
        System.out.println("CustomAuthenticationFilter password = " + request.getAttribute("password").toString());
        String username = request.getAttribute("username").toString();
        String password = request.getAttribute("password").toString();



//        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
//        String username = (String) inputFlashMap.get("email");
//        String password = (String) inputFlashMap.get("password");

//        HttpSession session = request.getSession();
//        String username = (String) session.getAttribute("email");
//        String password = (String) session.getAttribute("password");
//
//        System.out.println("username = " + username);
//        System.out.println("password = " + password);




//        String username =request.getHeader("email");
//        String password = request.getHeader("password");

        //username, password 로 토큰생성
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        System.out.println("token = " + token);
        System.out.println("authenticationManager = " + authenticationManager);
        //CustomAuthProvider 의 authenticate 에서 토큰을 검사
        Authentication authenticate = authenticationManager.authenticate(token);
        System.out.println("authenticate = " + authenticate);

        //여기까지 성공하면 KakaoService로 값 리턴
        return authenticationManager.authenticate(token);
    }


    
}
