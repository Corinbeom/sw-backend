package com.sw.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

@Service
public class FootballService {


    private String apiKey;

    public FootballService() {
        Dotenv dotenv = Dotenv.load();
        this.apiKey = dotenv.get("FOOTBALL_DATA_API");
    }

    public String getCompetitions() {
        try {


            // Create the HTTP client
            HttpClient client = HttpClient.newHttpClient();

            // Build the HTTP GET request with additional headers
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.football-data.org/v4/competitions"))
                    .header("X-Auth-Token", apiKey)
                    .header("User-Agent", "Java HTTP Client")
                    .header("Accept", "application/json")
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error occurred while fetching competitions.";
        }
    }

    public String getCompetitionMatches(String competitionId) {
        try {

            // Create the HTTP client
            HttpClient client = HttpClient.newHttpClient();

            // Build the HTTP GET request with additional headers
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.football-data.org/v4/competitions/" + competitionId + "/matches"))
                    .header("X-Auth-Token", apiKey)
                    .header("User-Agent", "Java HTTP Client")
                    .header("Accept", "application/json")
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "{\"error\": \"Error occurred while fetching matches.\"}";
        }
    }
}
