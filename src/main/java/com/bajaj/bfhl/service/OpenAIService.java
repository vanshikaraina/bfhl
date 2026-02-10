package com.bajaj.bfhl.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    private static final String OPENAI_URL =
            "https://api.openai.com/v1/responses";

    public String askAI(String question) {

        // SAFETY: don't crash app
        if (apiKey == null || apiKey.isBlank()) {
            return "Unavailable";
        }

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("model", "gpt-4o-mini");
            body.put(
                    "input",
                    "Answer in ONE WORD only. No explanation.\nQuestion: " + question
            );

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(body, headers);

            Map response = restTemplate.postForObject(
                    OPENAI_URL,
                    request,
                    Map.class
            );

            List output = (List) response.get("output");
            Map first = (Map) output.get(0);
            List content = (List) first.get("content");
            Map textObj = (Map) content.get(0);

            return textObj.get("text").toString().trim();

        } catch (Exception e) {
            return "Unavailable";
        }
    }
}