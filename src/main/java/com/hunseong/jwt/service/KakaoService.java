package com.hunseong.jwt.service;



import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hunseong.jwt.security.CustomAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Service
@Setter
@RequiredArgsConstructor
public class KakaoService  {


    // SecurityConfig 의 Configure 에서 주입 받는다
    private CustomAuthenticationFilter customAuthenticationFilter;   //인증처리
    private  AuthenticationFailureHandler authenticationFailureHandler;  //인증 실패처리
    private  AuthenticationSuccessHandler authenticationSuccessHandler;  //인증 성공처리

//    public KakaoService(CustomAuthenticationFilter customAuthenticationFilter,
//                        AuthenticationFailureHandler authenticationFailureHandler,
//                        AuthenticationSuccessHandler authenticationSuccessHandler) {
//        System.out.println("customAuthenticationFilter 확인3 = " + customAuthenticationFilter);
//
//
//        this.customAuthenticationFilter=customAuthenticationFilter;
//        this.authenticationFailureHandler=authenticationFailureHandler;
//        this.authenticationSuccessHandler=authenticationSuccessHandler;
//    }






    // 로그인 처리 후 JWT토큰 발급
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String username = request.getAttribute("username").toString();
        System.out.println("KakaoService username = " + username);
        System.out.println("customAuthenticationFilter 확인2 = " + customAuthenticationFilter);



        try {
            //CustomAuthenticationFilter 의 attemptAuthentication 으로 이동
            //request 에는  username( = 아이디(email) ),password 정보가 들어있음
            Authentication authentication = customAuthenticationFilter.attemptAuthentication(request, response);

            //CustomAuthenticationFilter 의 attemptAuthentication 로 부터 넘어온 authentication 을 넣어준다
            //CustomSuccessHandler 로 이동
            authenticationSuccessHandler.onAuthenticationSuccess(request,response,authentication);
        }

        catch (AuthenticationException e){

            //AuthenticationFailureHandler 로 이동
            authenticationFailureHandler.onAuthenticationFailure(request,response,e);
        }

//        customAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
//        customAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        System.out.println("나오니?");
    }


    public String getAccessToken (String authorize_code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //    POST 요청을 위해 기본값이 false인 setDoOutput을 true로

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //    POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=111e6bb27d40bdb8fee9064791def867");  //본인이 발급받은 key
            sb.append("&redirect_uri=http://localhost:8080/kakao/login");     // 본인이 설정해 놓은 경로
            sb.append("&code=" + authorize_code);
            bw.write(sb.toString());
            bw.flush();

            //    결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);



            //    요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //    Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return access_Token;
    }




    public HashMap<String, Object> getUserInfo (String access_Token) {

        //    요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            //    요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);


            Long id= element.getAsJsonObject().get("id").getAsLong();
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();


            String nickname = properties.getAsJsonObject().get("nickname").getAsString();

            String email = kakao_account.getAsJsonObject().get("email").getAsString();


            userInfo.put("id", id);
            userInfo.put("nickname", nickname);
            userInfo.put("email", email);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return userInfo;
    }
}
