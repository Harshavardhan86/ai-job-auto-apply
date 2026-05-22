package com.harsha.autoapply.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harsha.autoapply.service.EmailService;
import com.harsha.autoapply.service.FileUploadService;
import com.harsha.autoapply.service.GroqService;
import com.harsha.autoapply.service.OCRService;
import com.harsha.autoapply.service.PDFExtractorService;

@RestController
@RequestMapping("api/autoapply")
@CrossOrigin("*")
public class AutoApplyController {

    @Autowired //extract text from jpg, png
    private OCRService ocrService;

    @Autowired //call groq,prompt for resume screenshot and body
    private GroqService groqService;

    @Autowired  //get file name & path
    private FileUploadService fileUploadService;

    @Autowired //extract text from doc
    private PDFExtractorService pdfExtractorService;

    @Autowired //send mail using javaMail
    private EmailService emailService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/apply")
    public ResponseEntity<String> apply(
            @RequestParam("jobScreenshot") MultipartFile jobScreenshot,
            @RequestParam("resume") MultipartFile resume) {

        try {

            String jobFileName = fileUploadService.uploadScreenshot(jobScreenshot);
            String jobImagePath = fileUploadService.getScreenshotPath(jobFileName);

            String jobOcrText = ocrService.extractText(jobImagePath);
            if (jobOcrText.startsWith("ERROR")) {
                return ResponseEntity.badRequest()
                        .body("Job screenshot OCR failed: " + jobOcrText);
            }

            String jobJson = groqService.extractJobDetails(jobOcrText);
            JsonNode jobNode = objectMapper.readTree(jobJson);

            String companyName = jobNode.path("companyName").asText();
            String jobRole     = jobNode.path("jobRole").asText();
            String hrEmail     = jobNode.path("hrEmail").asText();

            if (hrEmail.isBlank() || hrEmail.equals("null")) {
                return ResponseEntity.badRequest()
                        .body("HR email not found in job post");
            }

            String resumeFileName = fileUploadService.uploadResume(resume);
            String resumePath     = fileUploadService.getResumePath(resumeFileName);

            String resumeText;
            String lowerName = resume.getOriginalFilename().toLowerCase();
            if (lowerName.endsWith(".pdf")) {
                resumeText = pdfExtractorService.extractText(resumePath);
            } else {
                resumeText = ocrService.extractText(resumePath);
            }

            if (resumeText == null || resumeText.isBlank()) {
                return ResponseEntity.badRequest()
                        .body("Could not extract text from resume");
            }

            String resumeJson = groqService.extractResumeDetails(resumeText);
            JsonNode resumeNode = objectMapper.readTree(resumeJson);

            String candidateName = resumeNode.path("fullName").asText();

            // Build skills string
            StringBuilder skillsBuilder = new StringBuilder();
            resumeNode.path("skills").forEach(skill ->
                skillsBuilder.append(skill.asText()).append(", "));
            String skills = skillsBuilder.toString()
                    .replaceAll(", $", "");

            // Build education string
            String education = "";
            JsonNode eduNode = resumeNode.path("education");
            if (eduNode.isArray() && eduNode.size() > 0) {
                JsonNode latest = eduNode.get(0);
                education = latest.path("degree").asText()
                        + " from " + latest.path("college").asText()
                        + " (" + latest.path("year").asText() + ")";
            }

            String emailBody = groqService.generateEmailBody(
                    candidateName,
                    jobRole,
                    companyName,
                    skills,
                    education);

            emailService.sendApplicationEmail(
                    hrEmail,
                    candidateName,
                    jobRole,
                    companyName,
                    resumePath,
                    emailBody);

            String summary = """
                    {
                      "status": "success",
                      "message": "Application sent successfully",
                      "sentTo": "%s",
                      "candidate": "%s",
                      "jobRole": "%s",
                      "company": "%s"
                    }
                    """.formatted(hrEmail, candidateName, jobRole, companyName);

            return ResponseEntity.ok(summary);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Auto apply failed: " + e.getMessage());
        }
    }
}