package com.example.demo.models;

import java.util.Map;

public class ProductResponseDTO {
    private String name;
    private Double popularityScore;
    private Double weight;
    private Map<String, String> images;
    private Double price;
    private Double popularityRating; // Out of 5

    // Constructors
    public ProductResponseDTO() {}

    public ProductResponseDTO(String name, Double popularityScore, Double weight, 
                             Map<String, String> images, Double price, Double popularityRating) {
        this.name = name;
        this.popularityScore = popularityScore;
        this.weight = weight;
        this.images = images;
        this.price = price;
        this.popularityRating = popularityRating;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPopularityScore() {
        return popularityScore;
    }

    public void setPopularityScore(Double popularityScore) {
        this.popularityScore = popularityScore;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Map<String, String> getImages() {
        return images;
    }

    public void setImages(Map<String, String> images) {
        this.images = images;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPopularityRating() {
        return popularityRating;
    }

    public void setPopularityRating(Double popularityRating) {
        this.popularityRating = popularityRating;
    }
}
