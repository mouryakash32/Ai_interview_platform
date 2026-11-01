package com.art.ai_interview_platform_backend.entity;

import com.art.ai_interview_platform_backend.entity.resumeEntity.Resume;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Set;
@Entity
@Data
public class User {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Roles> roles;
    private String storageFolder;

    @ToString.Exclude
    @OneToOne(mappedBy = "user")
    private Resume resumes;
}
