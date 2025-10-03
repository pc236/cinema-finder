package com.choe.cinema_finder.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.choe.cinema_finder.dto.TMDBResponse;

@Service
public class TMDBService {
    
    @Value("${tmdb.api.key}")
    private String apiKey;
    
    @Value("${tmdb.api.base-url}")
    private String baseUrl;
    
    private final RestTemplate restTemplate;
    
    public TMDBService() {
        this.restTemplate = new RestTemplate();
    }
    
    public TMDBResponse searchMovies(String query) {
        String url = String.format("%s/search/movie?api_key=%s&query=%s", 
                                    baseUrl, apiKey, query);
        return restTemplate.getForObject(url, TMDBResponse.class);
    }
    
    public TMDBResponse getTrendingMovies() {
        String url = String.format("%s/trending/movie/week?api_key=%s", 
                                    baseUrl, apiKey);
        return restTemplate.getForObject(url, TMDBResponse.class);
    }
    
    public TMDBResponse getPopularMovies() {
        String url = String.format("%s/movie/popular?api_key=%s", 
                                    baseUrl, apiKey);
        return restTemplate.getForObject(url, TMDBResponse.class);
    }
}