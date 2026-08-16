package com.art.ai_interview_platform_backend.resumeParseInfo;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
@Component
public class ExtractParticularSection {
    Set<String> skillSet = Set.of(
            "java",
            "spring boot",
            "spring",
            "hibernate",
            "mysql",
            "postgresql",
            "mongodb",
            "docker",
            "kubernetes",
            "kafka",
            "redis",
            "react",
            "javascript",
            "python",
            "c++",
            "git",
            "aws",
            "js",
            "sql",
            "c#"
    );

    public List<String> matchedSkills(List<String> skillsText){
        List<String>matchedSkills = new ArrayList<>();
        for(String skills: skillsText) {
            List<String> skillList = Arrays.stream(
                            skills.split("[,;:\\n|]")
                    )
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();

            skillList.forEach(skill -> {
                if (skillSet.contains(skill.toLowerCase())) matchedSkills.add(skill);
            });
        }
        return matchedSkills;
    }




}
