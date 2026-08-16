package com.art.ai_interview_platform_backend.entity;

import lombok.Data;

import java.util.List;

@Data
public class Experience {
    private String company;
    private String role;
    private String startDate;
    private String endDate;
    private List<String> techanologies;
    private List<String> responsibilities;
}
