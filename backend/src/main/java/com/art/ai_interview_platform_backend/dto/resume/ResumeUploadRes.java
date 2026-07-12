package com.art.ai_interview_platform_backend.dto.resume;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumeUploadRes {
    public int status;
    public String message;
}
