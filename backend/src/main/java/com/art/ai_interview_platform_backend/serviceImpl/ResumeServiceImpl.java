package com.art.ai_interview_platform_backend.serviceImpl;

import com.art.ai_interview_platform_backend.dto.resume.ResumeGetRes;
import com.art.ai_interview_platform_backend.dto.resume.ResumeUploadRes;
import com.art.ai_interview_platform_backend.entity.User;
import com.art.ai_interview_platform_backend.entity.resumeEntity.Resume;
import com.art.ai_interview_platform_backend.entity.resumeEntity.ResumeStatus;
import com.art.ai_interview_platform_backend.repository.ResumeRepo;
import com.art.ai_interview_platform_backend.repository.UserRepo;
import com.art.ai_interview_platform_backend.service.ResumeService;
import com.art.ai_interview_platform_backend.utils.ExtractResumeInformation;
import com.art.ai_interview_platform_backend.utils.GetLoggedInUser;
import com.art.ai_interview_platform_backend.utils.ResumeParseService;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.transform.URIResolver;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class ResumeServiceImpl implements ResumeService {
    private final GetLoggedInUser loggedInUser;
    private final ResumeRepo resumeRepo;
    private final UserRepo userRepo;
    private final ResumeParseService parseService;
    private final ExtractResumeInformation extractResumeInformation;

    public ResumeServiceImpl(GetLoggedInUser get, ResumeRepo resumeRepo, UserRepo userRepo, ResumeParseService parseService, ExtractResumeInformation extractResumeInformation) {
        this.loggedInUser = get;
        this.resumeRepo = resumeRepo;
        this.userRepo = userRepo;
        this.parseService = parseService;
        this.extractResumeInformation = extractResumeInformation;
    }

    @Override
    public ResumeGetRes getResume() {
        User user = loggedInUser.getLoggedInUser();
        Optional<Resume> resume= null;
        try {
            resume = resumeRepo.findByUserId(user.getId());
            extractResumeInformation.ExtractInfo(resume.get().getExtractText());
        } catch (Exception e) {
            throw new RuntimeException(e + "Exception happened at finding the user and extracting the information of the resume");
        }
        if(resume.isPresent()){
            System.out.println("---------------------------Resume present----------------------------------------");
            Resume resume1 = resume.get();
            ResumeGetRes resumeGetRes = ResumeGetRes.builder()
                    .fileName(resume1.getFileName())
                    .statusCode(200)
                    .uploadedAt(resume1.getUploadedAt())
                    .message("Got the resume")
                    .status(resume1.getStatus())
                    .build();
            return resumeGetRes;
        }else {
            return ResumeGetRes.builder().statusCode(404).message("No resume data found").build();
        }

    }

    @Transactional
    @Override
    public ResumeUploadRes uploadResume(MultipartFile file) {
        try {
        // Validate File
        if(basicResumeValidation(file)) {

            // Get Logged In User
            User user = loggedInUser.getLoggedInUser();

            // Create User Directory
            Path userDirectory = Paths.get("uploads", user.getStorageFolder());
                if (!Files.exists(userDirectory)) {
                    Files.createDirectories(userDirectory);
                }

                // Destination Path
                Path destination = userDirectory.resolve(file.getOriginalFilename());

                Optional<Resume> optionalResume = resumeRepo.findByUserId(user.getId());

                // Update Existing Resume
                if (optionalResume.isPresent()) {

                    Resume existingResume = optionalResume.get();
                    Files.deleteIfExists(Paths.get(existingResume.getFilePath()));
                    Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
                    existingResume.setFileName(file.getOriginalFilename());
                    existingResume.setFilePath(destination.toString());
                    existingResume.setUploadedAt(LocalDateTime.now());
                    existingResume.setStatus(ResumeStatus.UPLOADED);
                    String text = parseService.extractText(destination);
                    existingResume.setExtractText(text);
                    resumeRepo.save(existingResume);
                    return new ResumeUploadRes(
                            200,
                            "Resume updated successfully."
                    );
                }
                // First Time Upload

                Files.copy(
                        file.getInputStream(),
                        destination,
                        StandardCopyOption.REPLACE_EXISTING
                );
                Resume resume = new Resume();

                resume.setUser(user);
                resume.setFileName(file.getOriginalFilename());
                resume.setFilePath(destination.toString());
                resume.setUploadedAt(LocalDateTime.now());
                resume.setStatus(ResumeStatus.UPLOADED);
                String text = parseService.extractText(destination);
                resume.setExtractText(text);
                resumeRepo.save(resume);

                return new ResumeUploadRes(
                        201,
                        "Resume uploaded successfully."
                );
            }else {
            return new ResumeUploadRes(
                    400,
                    "Some basic validation faild for the resume."
            );
        }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error while uploading resume.", e);

            }

    }

    @Override
    public ResumeGetRes downloadResume() {
        User user = loggedInUser.getLoggedInUser();
        Optional<Resume> resume= resumeRepo.findByUserId(user.getId());
        if(resume.isPresent()){
            Resume resume1 = resume.get();
            Path path = Paths.get(resume1.getFilePath());
            try {
                UrlResource resource = new UrlResource(path.toUri());
                if(!resource.exists()){
                    throw new RuntimeException("Resume file not found.");
                }
                return ResumeGetRes.builder().message("resume found!").resource(resource).statusCode(200).build();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }else {
            return ResumeGetRes.builder().statusCode(400).message("Resume not found").build();
        }
    }

    @Override
    public ResumeGetRes deleteReumse() {
        System.out.println("Enter into deleteResume method");
        User user = loggedInUser.getLoggedInUser();
        Optional<Resume> resume = resumeRepo.findByUserId(user.getId());
        if(resume.isPresent()){
            Resume resume1 = resume.get();
            Path path = Paths.get(resume1.getFilePath());
            try {
                resume1.setUser(null);
                user.setResumes(null);
                resumeRepo.delete(resume1);
                Files.deleteIfExists(path);
                return ResumeGetRes.builder().message("Delete successfully").statusCode(200).build();
            } catch (IOException e) {
                throw new RuntimeException("file doesn't exist", e);
            }
        }
        return ResumeGetRes.builder().message("Failed to delete").statusCode(400).build();
    }

    private boolean basicResumeValidation(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (!"application/pdf".equals(file.getContentType())) {
            throw new IllegalArgumentException("Only PDF files are allowed.");
        }

        long maxSize = 2 * 1024 * 1024;

        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("Maximum file size allowed is 2 MB.");
        }
        return true;
    }
}
