package com.art.ai_interview_platform_backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFound(
            UsernameNotFoundException ex,
            HttpServletRequest request) {



        return ResponseEntity.status(404).body("User not found");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request) {
        return ResponseEntity.status(401).body("password error");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object>handleRuntimeException(RuntimeException e){
        return ResponseEntity.status(404).body(e.getMessage()  + "Run time exception");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object>handleIllegalArgumentException(IllegalArgumentException e){
        return ResponseEntity.status(404).body(e.getMessage() + "IllegalArgumentException occur");
    }
}