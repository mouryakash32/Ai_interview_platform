package com.art.ai_interview_platform_backend.resumeParseInfo;

import com.art.ai_interview_platform_backend.dto.ResumeSection;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HeaderSet {

    private static final Set<String> EXPERIENCE_HEADINGS = Set.of(
            "experience",
            "work experience",
            "professional experience",
            "employment history"
    );

    private static final Set<String> PROJECT_HEADINGS = Set.of(
            "projects",
            "personal projects",
            "academic projects"
    );

    private static final Set<String> SKILL_HEADINGS = Set.of(
            "technical skills",
            "skills",
            "technical expertise",
            "technical proficiencies"
    );

    private static final Set<String> EDUCATION_HEADINGS = Set.of(
            "education",
            "educational background",
            "academic background"
    );

    private static final Set<String> ACHIEVEMENT_HEADINGS = Set.of(
            "achievements",
            "accomplishments",
            "awards"
    );

    private static final Set<String> CERTIFICATION_HEADINGS = Set.of(
            "certifications",
            "certificates",
            "certifications & awards"
    );

    public ResumeSection detectResumeSection(String line){
        String normalize = line.trim().toLowerCase();
        if(EXPERIENCE_HEADINGS.contains(normalize)){
            return ResumeSection.EXPERIENCE;
        }
        if(CERTIFICATION_HEADINGS.contains(normalize)){
            return ResumeSection.CERTIFICATIONS;
        }
        if(PROJECT_HEADINGS.contains(normalize)){
            return ResumeSection.PROJECTS;
        }
        if(ACHIEVEMENT_HEADINGS.contains(normalize)){
            return ResumeSection.ACHIEVEMENTS;
        }
        if(SKILL_HEADINGS.contains(normalize)){
            return ResumeSection.SKILLS;
        }
        if(EDUCATION_HEADINGS.contains(normalize)){
            return ResumeSection.EDUCATION;
        }
        return ResumeSection.UNKNOWN;
    }

    public Map<ResumeSection, List<String>>extractSections(String rawText){
        Map<ResumeSection, List<String>> sectionListMap = new LinkedHashMap<>();
        for(ResumeSection section : ResumeSection.values()){
            sectionListMap.put(section, new ArrayList<>());
        }

        ResumeSection currentSection = ResumeSection.UNKNOWN;

        String [] lines = rawText.split("\\R");
        for(String line : lines){
            line = line.trim();
            if(line.isEmpty()){
                continue;
            }

            ResumeSection detectSection = detectResumeSection(line);
            if(detectSection != ResumeSection.UNKNOWN){
                currentSection = detectSection;
                continue;
            }
            sectionListMap.get(currentSection).add(line);

        }
        return sectionListMap;
    }
}
