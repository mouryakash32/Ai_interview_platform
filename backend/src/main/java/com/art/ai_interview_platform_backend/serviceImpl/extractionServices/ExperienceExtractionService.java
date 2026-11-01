package com.art.ai_interview_platform_backend.serviceImpl.extractionServices;

import com.art.ai_interview_platform_backend.dto.experience.ExperienceResDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExperienceExtractionService {

    private static final Logger log =
            LoggerFactory.getLogger(ExperienceExtractionService.class);

    private final ChatClient chatClient;

    public ExperienceExtractionService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    public ExperienceResDto extractExperience(List<String> experience) {

        if (experience == null || experience.isEmpty()) {
            throw new IllegalArgumentException(
                    "Experience section cannot be empty"
            );
        }

        try {
            String experienceStr = String.join("\n", experience);

            ExperienceResDto experienceResDto = chatClient.prompt()
                    .system("""
                            You are an expert resume information extraction system.

                            Extract all professional work experiences explicitly
                            mentioned in the supplied resume section.

                            For each experience, extract:
                            - jobTitle
                            - companyName
                            - location
                            - startDate
                            - endDate
                            - responsibilities

                            Rules:
                            1. Extract every distinct work experience.
                            2. Do not merge different experiences.
                            3. Extract only explicitly mentioned information.
                            4. Never invent or assume details.
                            5. Return null for missing fields.
                            6. Return an empty responsibilities list if none are mentioned.
                            7. Preserve the original date format.
                            8. Use "Present" only when ongoing employment is explicit.
                            9. Include internships and contract positions.
                            10. Ignore education, certifications and unrelated sections.
                            11. Return experiences in their original order.

                            Return ONLY valid JSON in this structure:

                            {
                              "experiences": [
                                {
                                  "jobTitle": "Software Developer",
                                  "companyName": "ABC Technologies",
                                  "location": "Hyderabad, India",
                                  "startDate": "January 2024",
                                  "endDate": "December 2024",
                                  "responsibilities": [
                                    "Developed REST APIs using Spring Boot."
                                  ]
                                }
                              ]
                            }

                            If no experience is found, return:
                            {"experiences": []}

                            Do not use Markdown code fences.
                            Do not include explanations.
                            """)
                    .user(experienceStr)
                    .call()
                    .entity(ExperienceResDto.class);

            return experienceResDto;


        } catch (Exception e) {
            log.error("Error while extracting experience from resume", e);

            throw new RuntimeException(
                    "Failed to extract experience from resume", e
            );
        }
    }
}