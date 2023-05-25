import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
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

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest
public class WordFrequencyAnalyzerImplIT {

    final static String BASE_URL = "http://localhost:8080/WordCountApplication-1.0-SNAPSHOT/api/wordfrequency";

    @Test
    void testCalculateHighestFrequency(WireMockRuntimeInfo wmRuntimeInfo) throws IOException, InterruptedException {
        String paramValue = "Hello Hello hello my name name nAme nAme nAme is IS is John";
        String encodedParam = URLEncoder.encode(paramValue, StandardCharsets.UTF_8);

        String responseBody = "5";

        String apiUrl = String.format("%s/highest-frequency",BASE_URL);
        String urlWithParams = String.format("%s?text=%s", apiUrl, encodedParam);

        //Define stub
        stubFor(get(apiUrl).withQueryParam("text", equalTo(paramValue)).willReturn(WireMock.ok(responseBody)));

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
        String paramValue = "Hello Hello hello my name name nAme nAme nAme is IS is John";
        String paramValue2 = "is";

        String encodedParam = URLEncoder.encode(paramValue, StandardCharsets.UTF_8);
        String encodedParam2 = URLEncoder.encode(paramValue2, StandardCharsets.UTF_8);

        String responseBody = "3";
        String apiUrl = String.format("%s/frequency-word",BASE_URL);
        String urlWithParams = String.format("%s?text=%s&word=%s", apiUrl, encodedParam,encodedParam2);

        //Define stub
        stubFor(get(apiUrl).withQueryParam("text", equalTo(paramValue)).willReturn(WireMock.ok(responseBody)));

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
        String paramValue = "Hello Hello hello my name name nAme nAme nAme is IS is John";
        String paramValue2 = "3";

        String encodedParam = URLEncoder.encode(paramValue, StandardCharsets.UTF_8);

        String apiUrl = String.format("%s/most-frequent-nwords",BASE_URL);
        String urlWithParams = String.format("%s?text=%s&n=%s", apiUrl, encodedParam,paramValue2);

        //Define stub
       // stubFor(get(apiUrl).withQueryParam("text", equalTo(paramValue)).willReturn(WireMock.ok(responseBody)));

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
