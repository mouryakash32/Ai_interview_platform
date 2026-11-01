package com.art.ai_interview_platform_backend.dto.experience;

import lombok.Data;

import java.util.List;

@Data
public class ExperienceDto {
    private String jobTitle;
    private String companyName;
    private String location;
    private String startDate;
    private String endDate;
    private List<String> responsibilities;
}
