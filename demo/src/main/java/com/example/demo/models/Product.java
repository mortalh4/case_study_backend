package com.example.demo.models;

import java.util.Map;

public class Product {
    private String name;
    private Double popularityScore;
    private Double weight;
    private Map<String, String> images;

    // Default constructor
    public Product() {}

    // Constructor with parameters
    public Product(String name, Double popularityScore, Double weight, Map<String, String> images) {
        this.name = name;
        this.popularityScore = popularityScore;
        this.weight = weight;
        this.images = images;
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

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", popularityScore=" + popularityScore +
                ", weight=" + weight +
                ", images=" + images +
                '}';
    }
}
