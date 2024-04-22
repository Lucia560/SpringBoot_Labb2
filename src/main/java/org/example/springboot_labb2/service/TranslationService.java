package org.example.springboot_labb2.service;

/**
 * @author Angela Gustafsson, anggus-1
 */
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class TranslationService {

    @Value("${libretranslate.api.url}")
    private String libreTranslateUrl;

    public String translate(String text, String sourceLang, String targetLang) throws IOException {
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(libreTranslateUrl);
        post.setHeader("Content-Type", "application/json");

        StringEntity entity = new StringEntity("{\"q\":\"" + text + "\",\"source\":\"" + sourceLang + "\",\"target\":\"" + targetLang + "\"}");
        post.setEntity(entity);

        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(result.toString());
        JsonNode translatedTextNode = jsonNode.get("translatedText");
        if (translatedTextNode != null) {
            return translatedTextNode.asText();
        } else {
            return "Translation not found"; // Or handle this case in a different way
        }

    }
}