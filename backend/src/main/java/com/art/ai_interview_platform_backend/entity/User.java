package com.art.ai_interview_platform_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

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
}
