package com.example.demo.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GoldPriceService {
    
    @Value("${gold.api.key:goldapi-2vng26qsmcssr58b-io}")
    private String apiKey;
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    // Cache variables
    private Double cachedGoldPrice;
    private LocalDateTime lastFetchTime;
    private static final long CACHE_DURATION_MINUTES = 30; // Cache for 30 minutes
    
    public GoldPriceService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }
    
    public Double getCurrentGoldPrice() {
        if (cachedGoldPrice != null && lastFetchTime != null && 
            ChronoUnit.MINUTES.between(lastFetchTime, LocalDateTime.now()) < CACHE_DURATION_MINUTES) {
            return cachedGoldPrice;
        }
        
        try {
            String url = "https://www.goldapi.io/api/XAU/USD";
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-access-token", apiKey);
            headers.set("Content-Type", "application/json");
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class
            );
            
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            
            // Get the price per ounce and convert to price per gram
            Double pricePerOunce = jsonNode.get("price").asDouble();
            Double pricePerGram = pricePerOunce / 31.1035; // 1 troy ounce = 31.1035 grams
            
            // Cache the result
            cachedGoldPrice = pricePerGram;
            lastFetchTime = LocalDateTime.now();
            
            return pricePerGram;
            
        } catch (Exception e) {
            System.err.println("Error fetching gold price: " + e.getMessage());
            // Return a fallback price (current approximate gold price per gram)
            return 65.0; // Fallback price in USD per gram
        }
    }
    
    public boolean isCacheValid() {
        return cachedGoldPrice != null && lastFetchTime != null && 
               ChronoUnit.MINUTES.between(lastFetchTime, LocalDateTime.now()) < CACHE_DURATION_MINUTES;
    }
    
    public void clearCache() {
        cachedGoldPrice = null;
        lastFetchTime = null;
    }
}