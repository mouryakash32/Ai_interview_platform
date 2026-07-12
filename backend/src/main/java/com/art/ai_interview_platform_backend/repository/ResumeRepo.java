package com.art.ai_interview_platform_backend.repository;

import com.art.ai_interview_platform_backend.entity.User;
import com.art.ai_interview_platform_backend.entity.resumeEntity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeRepo extends JpaRepository<Resume, Long> {
    Optional<Resume>findByUser(User user);
}
