package com.harsha.autoapply.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.harsha.autoapply.service.FileUploadService;
import com.harsha.autoapply.service.GroqService;
import com.harsha.autoapply.service.OCRService;
import com.harsha.autoapply.service.PDFExtractorService;

@RestController
@RequestMapping("api/resume")
@CrossOrigin("*")
public class ResumeOCRController {

    @Autowired
    private OCRService ocrService;

    @Autowired
    private GroqService groqService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private PDFExtractorService pdfExtractorService;

    @PostMapping("/upload-and-extract")
    public ResponseEntity<String> uploadAndExtract(
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file provided");
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null) {
            return ResponseEntity.badRequest().body("Invalid file name");
        }

        String lowerName = originalName.toLowerCase();
        boolean isPdf = lowerName.endsWith(".pdf");
        boolean isImage = lowerName.endsWith(".jpg")
                || lowerName.endsWith(".jpeg")
                || lowerName.endsWith(".png");

        if (!isPdf && !isImage) {
            return ResponseEntity.badRequest()
                    .body("Only PDF, JPG, and PNG files are supported");
        }

        String savedFileName;
        try {
            savedFileName = fileUploadService.uploadResume(file);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("File upload failed: " + e.getMessage());
        }

        String filePath = fileUploadService.getResumePath(savedFileName);

        String extractedText;

        if (isPdf) {
 
            try {
                extractedText = pdfExtractorService.extractText(filePath);
            } catch (Exception e) {
                return ResponseEntity.internalServerError()
                        .body("PDF extraction failed: " + e.getMessage());
            }
        } else {

            extractedText = ocrService.extractText(filePath);
            if (extractedText.startsWith("ERROR")) {
                return ResponseEntity.badRequest()
                        .body("OCR failed: " + extractedText);
            }
        }

        if (extractedText == null || extractedText.isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Could not extract any text from the file");
        }

        try {
            String result = groqService.extractResumeDetails(extractedText);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("AI extraction failed: " + e.getMessage());
        }
    }
}