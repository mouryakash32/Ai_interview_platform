package com.art.ai_interview_platform_backend.dto.auth;

import lombok.Data;

@Data
public class RegisterReq {
    String name;
    String email;
    String password;
}
