package com.art.ai_interview_platform_backend.controller;

import com.art.ai_interview_platform_backend.dto.resume.ResumeGetRes;
import com.art.ai_interview_platform_backend.dto.resume.ResumeUploadRes;
import com.art.ai_interview_platform_backend.repository.UserRepo;
import com.art.ai_interview_platform_backend.service.ResumeService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("user/resume")
public class ResumeController {
    private final ResumeService resumeService;
    public ResumeController( ResumeService resumeService) {
        this.resumeService = resumeService;
    }
    @GetMapping("/getResume")
    ResponseEntity<Object>getResume(){
        ResumeGetRes resumeGetRes = resumeService.getResume();
        return ResponseEntity.status(resumeGetRes.getStatusCode()).body(resumeGetRes);
    }

    @PostMapping("/upload")
    ResponseEntity<ResumeUploadRes> uploadResume(@RequestParam("file")MultipartFile file){
        ResumeUploadRes resumeUploadRes = resumeService.uploadResume(file);
        return ResponseEntity.status(resumeUploadRes.status).body(resumeUploadRes);
    }

    @GetMapping("/download")
    ResponseEntity<Resource> downloadResume(){
        ResumeGetRes resumeGetRes = resumeService.downloadResume();
        return ResponseEntity.status(resumeGetRes.statusCode).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resume.pdf\"").contentType(MediaType.APPLICATION_PDF).body(resumeGetRes.resource);
    }

    @DeleteMapping("/delete")
    ResponseEntity<ResumeGetRes> deleteResume(){
        ResumeGetRes resumeGetRes = resumeService.deleteReumse();
        return ResponseEntity.status(resumeGetRes.statusCode).body(resumeGetRes);
    }
}
