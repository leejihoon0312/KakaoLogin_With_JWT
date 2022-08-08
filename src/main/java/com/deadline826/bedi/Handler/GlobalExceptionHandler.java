package com.deadline826.bedi.Handler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.deadline826.bedi.exception.CustomIOException;
import com.deadline826.bedi.exception.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> responseResponseEntity() {
        ErrorResponse errorResponse = new ErrorResponse(401, "ID 또는 비밀번호가 일치하지 않습니다.");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    // Refresh Token 만료
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> refreshTokenExpiredException() {
        ErrorResponse errorResponse = new ErrorResponse(401, "Refresh Token이 만료되었습니다. 다시 로그인을 진행하여 Token을 갱신해주세요.");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    // 잘못된 Refresh Token
    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ErrorResponse> refreshTokenVerificationException() {
        ErrorResponse errorResponse = new ErrorResponse(400, "유효하지 않은 Refresh Token 입니다.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 목표 수정, 삭제시 오류처리
    @ExceptionHandler(CustomIOException.class)
    public ResponseEntity<ErrorResponse> validException( CustomIOException exec) {

        HttpHeaders headers =new HttpHeaders();
        headers.setContentType(MediaType.valueOf(APPLICATION_JSON_VALUE));
        ErrorResponse errorResponse = new ErrorResponse(400, exec.getMessage());
        return new ResponseEntity<>(errorResponse, headers, SC_BAD_REQUEST);

    }
}
