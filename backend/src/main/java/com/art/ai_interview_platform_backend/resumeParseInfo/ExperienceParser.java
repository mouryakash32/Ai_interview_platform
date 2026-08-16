package com.art.ai_interview_platform_backend.resumeParseInfo;

import com.art.ai_interview_platform_backend.entity.Experience;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ExperienceParser {

    private final ExtractParticularSection extractParticularSection;



        private static final Pattern DATE_RANGE = Pattern.compile(
                "(?i)" +
                        "(Jan(?:uary)?|Feb(?:ruary)?|Mar(?:ch)?|Apr(?:il)?|" +
                        "May|Jun(?:e)?|Jul(?:y)?|Aug(?:ust)?|Sep(?:tember)?|" +
                        "Oct(?:ober)?|Nov(?:ember)?|Dec(?:ember)?)" +
                        "\\s+\\d{4}" +
                        "\\s*[-–—]\\s*" +
                        "(Present|" +
                        "(Jan(?:uary)?|Feb(?:ruary)?|Mar(?:ch)?|Apr(?:il)?|" +
                        "May|Jun(?:e)?|Jul(?:y)?|Aug(?:ust)?|Sep(?:tember)?|" +
                        "Oct(?:ober)?|Nov(?:ember)?|Dec(?:ember)?)\\s+\\d{4})"
        );

    public ExperienceParser(ExtractParticularSection extractParticularSection) {
        this.extractParticularSection = extractParticularSection;
    }

    public static Matcher findDateRange(String text) {
            return DATE_RANGE.matcher(text);
        }

        public List<Experience> buildExperience(List<String> blocks){
            List<Experience>experiences = new ArrayList<>();
            for(String block: blocks){
                Experience experience = new Experience();

                Matcher matcher = findDateRange(block);

                if (matcher.find()) {

                    String dateRange = matcher.group();

                    String[] dates =
                            dateRange.split("\\s*[-–—]\\s*");
                    experience.setStartDate(dates[0].trim());
                    experience.setEndDate(dates[1].trim());
                    List<String>singleBlock = new ArrayList<>();
                    singleBlock.add(block);
                    List<String> extractedSkills = new ArrayList<>();
                    extractedSkills = extractParticularSection.matchedSkills(singleBlock);
                    experience.setTechanologies(extractedSkills);
                }
                experiences.add(experience);
            }
            return experiences;
        }




    private boolean containsDateRange(String line) {
        return line.matches(
                ".*\\d{4}\\s*[-–—]\\s*(Present|\\d{4}).*"
        );
    }

    public List<String> splitExperienceBlock(List<String> experienceBlock){
        List<String> blocks = new ArrayList<>();
        StringBuilder currentBlock = new StringBuilder();
        for(String line: experienceBlock){
            line = line.trim();
            if(line.isEmpty()){
                continue;
            }
            if(findDateRange(line).find()){
                if(!currentBlock.isEmpty()){
                    blocks.add(currentBlock.toString().trim());
                }
                currentBlock = new StringBuilder();
            }
            currentBlock.append(line).append("\n");
        }


        // Add the LAST experience
        if (!currentBlock.isEmpty()) {
            blocks.add(currentBlock.toString().trim());
        }

        return blocks;

    }


}
