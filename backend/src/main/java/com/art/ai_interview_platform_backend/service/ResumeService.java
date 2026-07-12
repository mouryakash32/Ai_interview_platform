package com.art.ai_interview_platform_backend.service;

import com.art.ai_interview_platform_backend.dto.resume.ResumeGetRes;
import com.art.ai_interview_platform_backend.dto.resume.ResumeUploadRes;
import org.springframework.web.multipart.MultipartFile;

public interface ResumeService {

    ResumeGetRes getResume();
    ResumeUploadRes uploadResume(MultipartFile file);

    ResumeGetRes downloadResume();

    ResumeGetRes deleteReumse();
}
