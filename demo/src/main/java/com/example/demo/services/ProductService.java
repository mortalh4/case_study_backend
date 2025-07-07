package com.example.demo.services;

import com.example.demo.models.Product;
import com.example.demo.models.ProductResponseDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    
    @Autowired
    private GoldPriceService goldPriceService;
    
    private final ObjectMapper objectMapper;
    private List<Product> products;
    
    public ProductService() {
        this.objectMapper = new ObjectMapper();
        this.products = new ArrayList<>();
    }
    
    @PostConstruct
    public void loadProducts() {
        try {
            ClassPathResource resource = new ClassPathResource("products.json");
            products = objectMapper.readValue(
                resource.getInputStream(), 
                new TypeReference<List<Product>>() {}
            );
            System.out.println("Loaded " + products.size() + " products from JSON file");
        } catch (IOException e) {
            System.err.println("Error loading products: " + e.getMessage());
            products = new ArrayList<>();
        }
    }
    
    public List<ProductResponseDTO> getAllProducts() {
        Double goldPrice = goldPriceService.getCurrentGoldPrice();
        
        return products.stream()
                .map(product -> convertToDTO(product, goldPrice))
                .collect(Collectors.toList());
    }
    
    public ProductResponseDTO getProductById(int id) {
        if (id < 0 || id >= products.size()) {
            return null;
        }
        
        Double goldPrice = goldPriceService.getCurrentGoldPrice();
        Product product = products.get(id);
        
        return convertToDTO(product, goldPrice);
    }
    
    public List<ProductResponseDTO> getFilteredProducts(Double minPrice, Double maxPrice, 
                                                       Double minPopularity, Double maxPopularity) {
        Double goldPrice = goldPriceService.getCurrentGoldPrice();
        
        return products.stream()
                .map(product -> convertToDTO(product, goldPrice))
                .filter(dto -> {
                    boolean priceFilter = true;
                    boolean popularityFilter = true;
                    
                    if (minPrice != null && dto.getPrice() < minPrice) {
                        priceFilter = false;
                    }
                    if (maxPrice != null && dto.getPrice() > maxPrice) {
                        priceFilter = false;
                    }
                    if (minPopularity != null && dto.getPopularityScore() < minPopularity) {
                        popularityFilter = false;
                    }
                    if (maxPopularity != null && dto.getPopularityScore() > maxPopularity) {
                        popularityFilter = false;
                    }
                    
                    return priceFilter && popularityFilter;
                })
                .collect(Collectors.toList());
    }
    
    private ProductResponseDTO convertToDTO(Product product, Double goldPrice) {
        // Calculate price: (popularityScore + 1) * weight * goldPrice
        Double calculatedPrice = (product.getPopularityScore() + 1) * product.getWeight() * goldPrice;
        
        // Round to 2 decimal places
        BigDecimal roundedPrice = BigDecimal.valueOf(calculatedPrice)
                .setScale(2, RoundingMode.HALF_UP);
        
        // Convert popularity score to rating out of 5 (1 decimal place)
        Double popularityRating = product.getPopularityScore() * 5;
        BigDecimal roundedRating = BigDecimal.valueOf(popularityRating)
                .setScale(1, RoundingMode.HALF_UP);
        
        return new ProductResponseDTO(
            product.getName(),
            product.getPopularityScore(),
            product.getWeight(),
            product.getImages(),
            roundedPrice.doubleValue(),
            roundedRating.doubleValue()
        );
    }
    
    public int getProductCount() {
        return products.size();
    }
    
    public void reloadProducts() {
        loadProducts();
    }
}