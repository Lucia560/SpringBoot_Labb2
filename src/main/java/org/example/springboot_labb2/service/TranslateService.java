package org.example.springboot_labb2.service;

import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;
@Service
public class TranslateService {
    private final RestClient restClient;

    public TranslateService(RestClient restClient) {
        this.restClient = restClient;
    }


    @Retryable
    public boolean detectMessageLanguage(String text) {
        String responseBody = Objects.requireNonNull(restClient.post()
                .uri("http://localhost:5000/detect")
                .contentType(APPLICATION_JSON)
                .accept()
                .body(text) // funkar?
                .retrieve()
                .body(String.class));
        return !responseBody.contains("\"en\"");

    }


    @Retryable
    public String translateMessage(String textToTranslate) {
        String startLang = "sv";
        String finalLang = "en";

        if (detectMessageLanguage(textToTranslate)) {
            startLang = "sv";
            finalLang = "en";
        }

        String jsonString = String
                .format("{\"q\":\"%s\",\"source\":\"%s\",\"target\":\"%s\"}", textToTranslate, startLang, finalLang);

        return Objects.requireNonNull(restClient.post()
                        .uri("http://localhost:5000/translate")
                        .contentType(APPLICATION_JSON)
                        .accept()
                        .body(jsonString)
                        .retrieve()
                        .body(String.class))
                        .replaceAll("\"", "");
    }
}
