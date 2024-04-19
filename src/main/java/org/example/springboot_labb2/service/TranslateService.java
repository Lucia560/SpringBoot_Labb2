package org.example.springboot_labb2.service;

import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
@Service
public class TranslateService {
    private final WebClient webClient;

    public TranslateService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Retryable
    public boolean detectMessageLanguage(String text) {
        String responseBody = webClient.post()
                .uri("http://localhost:5000/detect")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue("{\"text\":\"" + text + "\"}")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return responseBody != null && responseBody.contains("\"en\"");
    }

    @Retryable
    public String translateMessage(String textToTranslate) {
         String targetLang = "en";

         if (!detectMessageLanguage("en")) {
            String jsonString = String.format("{\"q\":\"%s\",\"source\":\"%s\",\"target\":\"%s\"}", textToTranslate, targetLang);


            return webClient.post()
                    .uri("http://localhost:5000/translate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(jsonString)
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(response -> response.replaceAll("\"", ""))
                    .block();
        }
        return textToTranslate;

    }
}
