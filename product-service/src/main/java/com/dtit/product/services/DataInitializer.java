package com.dtit.product.services;

import com.dtit.product.model.Product;
import com.dtit.product.repositories.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final ProductRepository productRepository;
    private final RedisTemplate<String, List<Product>> redisTemplate;

    @PostConstruct
    public void preloadData() {
        insertTestDataIntoMongo();
        insertTrendingProductsIntoRedis();
    }

    public void insertTestDataIntoMongo() {
        if (productRepository.count() > 0) return;

        List<Product> products = List.of(
                new Product("1", "Wireless Headphones", "High-quality noise-canceling headphones.",
                        "https://picsum.photos/seed/1/300/300", 199.99, "Tech"),
                new Product("2", "Running Shoes", "Lightweight and comfortable running shoes.",
                        "https://picsum.photos/seed/2/300/300", 89.99, "Shoes"),
                new Product("3", "Smart Watch", "Monitor your fitness and stay connected.",
                        "https://picsum.photos/seed/3/300/300", 149.99, "Tech"),
                new Product("4", "Denim Jacket", "Classic denim jacket with modern fit.",
                        "https://picsum.photos/seed/4/300/300", 59.99, "Clothing")
        );

        productRepository.saveAll(products);
    }

    public void insertTrendingProductsIntoRedis() {
        List<Product> trending = productRepository.findAll().stream().limit(3).toList();
        redisTemplate.opsForValue().set("trending_products", trending);
    }
}
