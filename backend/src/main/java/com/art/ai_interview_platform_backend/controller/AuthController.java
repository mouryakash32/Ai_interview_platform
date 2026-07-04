package com.art.ai_interview_platform_backend.controller;

import com.art.ai_interview_platform_backend.dto.auth.LoginReq;
import com.art.ai_interview_platform_backend.dto.auth.RegisterReq;
import com.art.ai_interview_platform_backend.dto.auth.AuthRes;
import com.art.ai_interview_platform_backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    ResponseEntity<AuthRes> register(@RequestBody RegisterReq registerReq){
        AuthRes authRes = authService.register(registerReq);
        return ResponseEntity.status(authRes.getStatus()).body(authRes);
    }

    @PostMapping("/login")
    ResponseEntity<AuthRes>login(@RequestBody LoginReq loginReq){
        AuthRes authRes = authService.login(loginReq);
        return ResponseEntity.status(authRes.getStatus()).body(authRes);
    }
}
