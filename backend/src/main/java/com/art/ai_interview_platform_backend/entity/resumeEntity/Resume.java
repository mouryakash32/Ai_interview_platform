package com.art.ai_interview_platform_backend.entity.resumeEntity;

import com.art.ai_interview_platform_backend.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fileName;
    private String filePath;
    private LocalDateTime uploadedAt;
    @Enumerated(EnumType.STRING)
    private ResumeStatus Status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
