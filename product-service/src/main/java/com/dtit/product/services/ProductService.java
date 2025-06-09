package com.dtit.product.services;

import com.dtit.product.clients.InventoryClient;
import com.dtit.product.clients.InventoryItem;
import com.dtit.product.controllers.ProductController;
import com.dtit.product.controllers.responses.ProductCardResponse;
import com.dtit.product.controllers.responses.ProductDetailResponse;
import com.dtit.product.controllers.responses.ProductTrendingResponse;
import com.dtit.product.model.Product;
import com.dtit.product.repositories.ProductRepository;
import com.dtit.product.utils.CheckUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final RedisTemplate<String, List<Product>> redisTemplate;
    private final InventoryClient inventoryClient;

    private static final String TRENDING_PRODUCTS_KEY = "trending_products";

    public List<ProductCardResponse> getProductsByCategory(String category) {
        List<Product> products = productRepository.findByCategory(category);
        if (products.isEmpty()) {
            return Collections.emptyList();
        }

        return products.stream()
                .map(product -> new ProductCardResponse(
                        product.getId(),
                        product.getName(),
                        product.getImage(),
                        product.getPrice()
                ))
                .toList();
    }

    public List<ProductDetailResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> {
                    boolean inStock = CheckUtil.checkInStock(product, inventoryClient, log);
                    return new ProductDetailResponse(
                            product.getId(),
                            product.getName(),
                            product.getDescription(),
                            product.getImage(),
                            product.getPrice(),
                            product.getCategory(),
                            inStock
                    );
                })
                .toList();
    }

    public List<ProductTrendingResponse> getTrendingProducts() {
        // 1. Try to fetch from Redis
        List<ProductTrendingResponse> cached = getCachedTrendingProducts();
        if (!cached.isEmpty()) {
            return cached;
        }

        // 2. Fallback to MongoDB
        List<Product> trendingFromDb = productRepository.findTop10ByOrderByPriceDesc(); // Or any criteria
        List<ProductTrendingResponse> response = trendingFromDb.stream()
                .map(p -> new ProductTrendingResponse(
                        p.getId(),
                        p.getName(),
                        p.getImage(),
                        p.getPrice()
                ))
                .toList();

        // 3. Store in Redis
        redisTemplate.opsForValue().set(TRENDING_PRODUCTS_KEY, trendingFromDb, Duration.ofMinutes(30));
        return response;
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public List<ProductCardResponse> getProductsByIds(List<String> ids) {
        List<Product> products = productRepository.findAllById(ids);

        return products.stream()
                .map(product -> new ProductCardResponse(
                        product.getId(),
                        product.getName(),
                        product.getImage(),
                        product.getPrice()
                ))
                .toList();
    }


    @SuppressWarnings("unchecked")
    private List<ProductTrendingResponse> getCachedTrendingProducts() {
        Object cached = redisTemplate.opsForValue().get(TRENDING_PRODUCTS_KEY);

        if (cached == null) {
            return Collections.emptyList();
        }

        // Handle both List<Product> and List<LinkedHashMap> cases
        List<Product> products;
        if (cached instanceof List<?> list) {
            if (list.isEmpty()) {
                return Collections.emptyList();
            }

            // If first element is already a Product, we can cast directly
            if (!list.isEmpty() && list.get(0) instanceof Product) {
                products = (List<Product>) list;
            }
            // Otherwise convert LinkedHashMap to Product
            else {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                products = ((List<LinkedHashMap<String, Object>>) list).stream()
                        .map(map -> mapper.convertValue(map, Product.class))
                        .toList();
            }
        } else {
            throw new IllegalStateException("Unexpected cached data type: " + cached.getClass());
        }

        return products.stream()
                .map(p -> new ProductTrendingResponse(
                        p.getId(),
                        p.getName(),
                        p.getImage(),
                        p.getPrice()
                ))
                .toList();
    }

}
