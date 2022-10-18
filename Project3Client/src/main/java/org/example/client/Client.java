package org.example.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Client {
    public static void main(String[] args) {
        final String nameSensor = "SensorQwe";

        reg(nameSensor);

        Random random = new Random();

        for (int i = 0; i < 2000; i++){
            add(random.nextDouble() * 55.0, random.nextBoolean(), nameSensor);
        }
    }

    private static void reg( String nameSensor) {
        String url = "http://localhost:8080/sensors/registration";

        Map<String, Object> map = new HashMap<>();
        map.put("name", nameSensor);

        makeRequest(url, map);
    }

    private static void add(Double temperature, Boolean rain, String nameSensor) {
        final String url = "http://localhost:8080/measurements/add";
        Map<String, Object> map = new HashMap<>();
        map.put("temperature", temperature);
        map.put("rain", rain);
        map.put("sensor", Map.of("name", nameSensor));

        makeRequest(url, map);
    }

    private static void makeRequest(String url, Map<String, Object> map) {
        final RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> request = new HttpEntity<>(map, headers);

        try {
            restTemplate.postForObject(url, request, String.class);

            System.out.println("Success");
        } catch (HttpClientErrorException e) {
            System.out.println(e.getMessage());
        }
    }

}
