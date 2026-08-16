package com.art.ai_interview_platform_backend.utils;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class ResumeParseService {
    public String extractText(Path pdfpath){
        try {
            PDDocument pdDocument = Loader.loadPDF(pdfpath.toFile());
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            return pdfTextStripper.getText(pdDocument);

        } catch (IOException e) {
            throw new RuntimeException("Exception occur at the extraction of the text" + e);
        }

    }
}
