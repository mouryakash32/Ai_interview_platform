package com.art.ai_interview_platform_backend.dto.resume;

import com.art.ai_interview_platform_backend.entity.resumeEntity.ResumeStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.UrlResource;

import java.time.LocalDateTime;

@Data
@Builder
public class ResumeGetRes {
    public String fileName;
    public ResumeStatus status;
    public LocalDateTime uploadedAt;
    public Integer statusCode;
    public String message;
    public UrlResource resource;
}
