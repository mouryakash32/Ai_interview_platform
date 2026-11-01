package com.art.ai_interview_platform_backend.serviceImpl.extractionServices;

import com.art.ai_interview_platform_backend.dto.skillDto.SkillResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SkillExtractionService {

    private static final Logger log =
            LoggerFactory.getLogger(SkillExtractionService.class);

    private final ChatClient chatClient;

    public SkillExtractionService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public SkillResponseDto extractSkills(List<String> skillsText) {

        if (skillsText == null || skillsText.isEmpty()) {
            throw new IllegalArgumentException(
                    "Skills section cannot be empty"
            );
        }

        String skills = String.join("\n", skillsText);

        try {

            // Step 1: Get raw response from LLM
            SkillResponseDto response = chatClient.prompt()
                    .system("""
            You are an expert resume skill extraction system.

            Extract all technical and professional skills explicitly
            mentioned in the supplied resume section.

            Rules:
            1. Extract only explicitly mentioned skills.
            2. Never invent or assume skills.
            3. Remove duplicate skills.
            4. Preserve original skill names.
            5. Ignore unrelated sentences.
            6. Return an empty list if no skills are found.

            Return ONLY valid JSON in this format:
            {
                "skills": ["Java", "Spring Boot", "MySQL"]
            }

            Do not use Markdown code blocks.
            Do not include explanations.
            """)
                    .user(skills)
                    .call()
                    .entity(SkillResponseDto.class);

            return response;

            // Temporary: inspect response before DTO conversion


        } catch (Exception e) {

            log.error("Error while extracting skills from resume", e);

            throw new RuntimeException(
                    "Failed to extract skills from resume", e
            );
        }
    }
}