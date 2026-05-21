package com.harsha.autoapply.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Service
public class GroqService {

    @Value("${xai.api.key}")
    private String apiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GroqService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.groq.com/openai")
                .build();
    }

 
    public String extractJobDetails(String extractedText) {

        String prompt = """
                Extract the following details from this job post:

                1. Company Name
                2. Job Role
                3. HR Email

                Return ONLY clean JSON, no markdown, no explanation:
                {
                  "companyName": "...",
                  "jobRole": "...",
                  "hrEmail": "..."
                }

                Job Post:
                """ + extractedText;

        return callGroq(prompt);
    }

    public String extractResumeDetails(String extractedText) {

        String prompt = """
                Extract the following details from this resume:

                1. Full Name
                2. Email
                3. Phone Number
                4. Skills (as a unique list, no duplicates)
                5. Education (degree, college, year)
                6. Experience (company, role, duration) — if any

                Return ONLY clean JSON, no markdown, no explanation:
                {
                  "fullName": "...",
                  "email": "...",
                  "phone": "...",
                  "skills": ["...", "..."],
                  "education": [
                    {
                      "degree": "...",
                      "college": "...",
                      "year": "..."
                    }
                  ],
                  "experience": [
                    {
                      "company": "...",
                      "role": "...",
                      "duration": "..."
                    }
                  ]
                }

                Resume Text:
                """ + extractedText;

        return callGroq(prompt);
    }

    private String callGroq(String prompt) {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama-3.3-70b-versatile");
        requestBody.put("temperature", 0.3);

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messages.add(userMessage);
        requestBody.put("messages", messages);

        String rawResponse = webClient.post()
                .uri("/v1/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(
                    status -> status.is4xxClientError() || status.is5xxServerError(),
                    clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(
                            new RuntimeException("xAI API error: " + errorBody)))
                )
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = objectMapper.readTree(rawResponse);
            String content = root.path("choices").get(0)
                    .path("message").path("content").asText();
           
            content = content.replaceAll("```json\\n?", "")
                             .replaceAll("```", "").trim();
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return rawResponse;
        }
    }
}