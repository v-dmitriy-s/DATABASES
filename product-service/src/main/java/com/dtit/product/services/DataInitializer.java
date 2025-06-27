package com.dtit.product.services;

import com.dtit.product.domain.*;
import com.dtit.product.repositories.ProductRepository;
import com.dtit.product.repositories.CategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final RedisTemplate<String, List<Product>> redisTemplate;

    @PostConstruct
    public void preloadData() {
        insertTestDataIntoMongo();
        insertTestCategoriesIntoMongo();
        insertTrendingProductsIntoRedis();
    }

    public void insertTestDataIntoMongo() {
        if (productRepository.count() > 0) return;

        List<Product> products = List.of(
                createProduct("1", "Wireless Headphones", "High-quality noise-canceling headphones.", "Tech", new BigDecimal("199.99")),
                createProduct("2", "Running Shoes", "Lightweight and comfortable running shoes.", "Shoes", new BigDecimal("89.99")),
                createProduct("3", "Smart Watch", "Monitor your fitness and stay connected.", "Tech", new BigDecimal("149.99")),
                createProduct("4", "Denim Jacket", "Classic denim jacket with modern fit.", "Clothing", new BigDecimal("59.99"))
        );

        productRepository.saveAll(products);
    }

    private Product createProduct(String id, String name, String description, String categoryName, BigDecimal basePrice) {
        Variant variant = new Variant();
        variant.setSku(id + "-SKU");
        variant.setPrice(basePrice);
        variant.setStockQuantity(100);
        Attribute color = new Attribute();
        color.setName("Color");
        color.setValue("Black");
        Attribute size = new Attribute();
        size.setName("Size");
        size.setValue("M");
        variant.setAttributeValues(List.of(color, size));
        Product product = new Product();
        product.setProductId(id);
        product.setName(name);
        product.setDescription(description);
        product.setCategoryName(categoryName);
        product.setBasePrice(basePrice);
        product.setPublished(true);
        product.setVariants(List.of(variant));
        return product;
    }

    public void insertTestCategoriesIntoMongo() {
        if (categoryRepository.count() > 0) return;
        Category tech = new Category();
        tech.setName("Tech");
        tech.setDescription("Technology and gadgets");
        Category phones = new Category();
        phones.setName("Phones");
        phones.setDescription("Smartphones and accessories");
        tech.setSubCategories(List.of(phones));

        Category clothing = new Category();
        clothing.setName("Clothing");
        clothing.setDescription("Apparel and fashion");
        Category jackets = new Category();
        jackets.setName("Jackets");
        jackets.setDescription("Outerwear");
        clothing.setSubCategories(List.of(jackets));

        categoryRepository.saveAll(List.of(tech, clothing));
    }

    public void insertTrendingProductsIntoRedis() {
        List<Product> trending = productRepository.findAll().stream().limit(3).toList();
        redisTemplate.opsForValue().set("trending_products", trending);
    }
}
