package com.art.ai_interview_platform_backend.utils;

import com.art.ai_interview_platform_backend.dto.ResumeSection;
import com.art.ai_interview_platform_backend.entity.Experience;
import com.art.ai_interview_platform_backend.resumeParseInfo.ExperienceParser;
import com.art.ai_interview_platform_backend.resumeParseInfo.ExtractParticularSection;
import com.art.ai_interview_platform_backend.resumeParseInfo.HeaderSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ExtractResumeInformation {
    private  final HeaderSet headerSet;
    private final ExtractParticularSection extractParticularSection;
    private final ExperienceParser experienceParser;
    public ExtractResumeInformation(HeaderSet headerSet, ExtractParticularSection extractParticularSection, ExperienceParser experienceParser) {
        this.headerSet = headerSet;
        this.extractParticularSection = extractParticularSection;
        this.experienceParser = experienceParser;
    }


    public String normalizeTest(String text){
        return text
                .replace("\u00A0", " ")   // non-breaking space
                .replaceAll("[\\p{Cc}&&[^\\r\\n\\t]]", "")
                .replaceAll("[ \\t]+", " ")
                .trim();
    }


    public void ExtractInfo(String text){
        String normalizeText = normalizeTest(text);
        Map<ResumeSection, List<String>> resumeSectionListMap = headerSet.extractSections(normalizeText);
        List<String> matchedSkills = extractParticularSection.matchedSkills(resumeSectionListMap.get(ResumeSection.SKILLS));
        List<String> experienceBlock = experienceParser.splitExperienceBlock(resumeSectionListMap.get(ResumeSection.EXPERIENCE));
        List<Experience> experiences = experienceParser.buildExperience(experienceBlock);
//        experience.forEach(System.out::println);
//        System.out.println(experience.get(0));
    }



}
