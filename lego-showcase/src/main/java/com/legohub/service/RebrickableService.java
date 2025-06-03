package com.legohub.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RebrickableService {
    @Value("${rebrickable.api.key}")
    private String apiKey;

    @Value("${rebrickable.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public RebrickableService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public JsonNode getSetByNumber(String setNumber) {
        String fullSetNumber = setNumber.endsWith("-1") ? setNumber : setNumber + "-1";
        String url = apiUrl + "/lego/sets/" + fullSetNumber + "/";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "key " + apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch LEGO set: " + setNumber);
        }
    }

    public String getThemeName(int themeId) {
        String url = apiUrl + "/lego/themes/" + themeId + "/";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "key " + apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode themeData = objectMapper.readTree(response.getBody());
            return themeData.get("name").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch LEGO theme: " + themeId);
        }
    }
}
