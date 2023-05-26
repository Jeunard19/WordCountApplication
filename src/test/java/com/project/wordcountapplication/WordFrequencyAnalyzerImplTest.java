package com.project.wordcountapplication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest
public class WordFrequencyAnalyzerImplTest {

    final static String BASE_URL = "http://localhost:8080/WordCountApplication-1.0-SNAPSHOT/api/wordfrequency";
    //Deals with special characters and spaces
    final static String TEXT = URLEncoder.encode("Hello Hello-hello''my(name name'nAme-nAme-nAme-is,IS is John",
            StandardCharsets.UTF_8);

    @Test
    void testCalculateHighestFrequency(WireMockRuntimeInfo wmRuntimeInfo) throws IOException, InterruptedException {

        String responseBody = "5";

        String apiUrl = String.format("%s/highest-frequency",BASE_URL);
        String urlWithParams = String.format("%s?text=%s", apiUrl, TEXT);

        // Make a request to the WireMock server
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlWithParams))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(),responseBody);

    }

    @Test
    void testCalculateFrequencyForWord(WireMockRuntimeInfo wmRuntimeInfo) throws IOException, InterruptedException {
        String word = "is";

        String responseBody = "3";
        String apiUrl = String.format("%s/frequency-word",BASE_URL);
        String urlWithParams = String.format("%s?text=%s&word=%s", apiUrl, TEXT,word);

        // Make a request to the WireMock server
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlWithParams))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(responseBody, response.body());
    }

    @Test
    void testCalculateMostFrequentNWords(WireMockRuntimeInfo wmRuntimeInfo) throws IOException, InterruptedException {
        int nWords= 3;

        String apiUrl = String.format("%s/most-frequent-nwords",BASE_URL);
        String urlWithParams = String.format("%s?text=%s&n=%s", apiUrl, TEXT,nWords);

        // Make a request to the WireMock server
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlWithParams))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Parse the JSON string into a JsonNode
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());

        assertEquals(5, jsonNode.get(0).get("frequency").asInt());
        assertEquals("name", jsonNode.get(0).get("word").asText());
        assertEquals(3, jsonNode.get(1).get("frequency").asInt());
        assertEquals("hello", jsonNode.get(1).get("word").asText());
        assertEquals(3, jsonNode.get(2).get("frequency").asInt());
        assertEquals("is", jsonNode.get(2).get("word").asText());

    }

}
