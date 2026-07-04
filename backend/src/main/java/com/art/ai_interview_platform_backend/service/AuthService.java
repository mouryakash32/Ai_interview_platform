package com.art.ai_interview_platform_backend.service;

import com.art.ai_interview_platform_backend.dto.auth.LoginReq;
import com.art.ai_interview_platform_backend.dto.auth.RegisterReq;
import com.art.ai_interview_platform_backend.dto.auth.AuthRes;

public interface AuthService {
   AuthRes register(RegisterReq registerReq);

    AuthRes login(LoginReq loginReq);
}
