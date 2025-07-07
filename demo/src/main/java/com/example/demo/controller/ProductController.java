package com.example.demo.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.ProductResponseDTO;
import com.example.demo.services.ProductService;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*") // Allow all origins for development
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        try {
            List<ProductResponseDTO> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            System.err.println("Error fetching products: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable int id) {
        try {
            ProductResponseDTO product = productService.getProductById(id);
            if (product == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            System.err.println("Error fetching product by ID: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/filter")
    public ResponseEntity<List<ProductResponseDTO>> getFilteredProducts(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minPopularity,
            @RequestParam(required = false) Double maxPopularity
    ) {
        try {
            List<ProductResponseDTO> filteredProducts = productService.getFilteredProducts(
                minPrice, maxPrice, minPopularity, maxPopularity
            );
            return ResponseEntity.ok(filteredProducts);
        } catch (Exception e) {
            System.err.println("Error filtering products: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/count")
    public ResponseEntity<Integer> getProductCount() {
        try {
            int count = productService.getProductCount();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            System.err.println("Error getting product count: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping("/reload")
    public ResponseEntity<String> reloadProducts() {
        try {
            productService.reloadProducts();
            return ResponseEntity.ok("Products reloaded successfully");
        } catch (Exception e) {
            System.err.println("Error reloading products: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}