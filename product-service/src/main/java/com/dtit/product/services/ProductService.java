package com.dtit.product.services;

import com.dtit.product.clients.InventoryClient;
import com.dtit.product.controllers.responses.ProductCardResponse;
import com.dtit.product.controllers.responses.ProductDetailResponse;
import com.dtit.product.controllers.responses.ProductTrendingResponse;
import com.dtit.product.domain.Product;
import com.dtit.product.domain.Variant;
import com.dtit.product.repositories.ProductRepository;
import com.dtit.product.utils.CheckUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import com.dtit.product.dto.ProductFilter;
import org.springframework.data.domain.PageRequest;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

/**
 * Service class for managing products and their variants.
 * Provides business logic for product CRUD operations, search, and variant management.
 * Integrates with MongoDB for persistence and Redis for caching trending products.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final RedisTemplate<String, List<Product>> redisTemplate;
    private final InventoryClient inventoryClient;

    private static final String TRENDING_PRODUCTS_KEY = "trending_products";

    /**
     * Retrieves trending products with caching support.
     * First attempts to fetch from Redis cache, then falls back to MongoDB.
     * Caches results for 30 minutes.
     *
     * @return List of trending products as ProductTrendingResponse
     */
    public List<ProductTrendingResponse> getTrendingProducts() {
        // 1. Try to fetch from Redis
        List<ProductTrendingResponse> cached = getCachedTrendingProducts();
        if (!cached.isEmpty()) {
            return cached;
        }

        // 2. Fallback to MongoDB
        List<Product> trendingFromDb = productRepository.findTop10ByOrderByBasePriceDesc(); // Or any criteria
        List<ProductTrendingResponse> response = trendingFromDb.stream()
                .map(p -> new ProductTrendingResponse(
                        p.getProductId(),
                        p.getName(),
                        null, // No image field in Product
                        p.getBasePrice().doubleValue()
                ))
                .toList();

        // 3. Store in Redis
        redisTemplate.opsForValue().set(TRENDING_PRODUCTS_KEY, trendingFromDb, Duration.ofMinutes(30));
        return response;
    }

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param productId The unique identifier of the product
     * @return Optional containing the product if found, empty otherwise
     */
    public Optional<Product> getProductById(String productId) {
        return productRepository.findById(productId);
    }

    /**
     * Retrieves multiple products by their IDs and converts them to ProductCardResponse.
     *
     * @param ids List of product identifiers
     * @return List of ProductCardResponse objects
     */
    public List<ProductCardResponse> getProductsByIds(List<String> ids) {
        List<Product> products = productRepository.findAllById(ids);

        return products.stream()
                .map(product -> new ProductCardResponse(
                        product.getProductId(),
                        product.getName(),
                        null, // No image field in Product
                        product.getBasePrice().doubleValue()
                ))
                .toList();
    }

    /**
     * Searches and filters products with pagination support using MongoDB queries.
     * Filters by name, category, price range, and attribute values at the database level.
     * Includes real-time stock status from inventory service.
     *
     * db.product.find({})
     *     .sort({ "name": 1 })  // 1 for ascending, -1 for descending
     *     .skip(40)            // Skips the documents from the previous pages
     *     .limit(20)           // Limits the result to the requested page size
     *
     * @param filter ProductFilter containing all search and filter criteria
     * @return Page of ProductDetailResponse with stock information
     */
    public Page<ProductDetailResponse> searchProducts(ProductFilter filter) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
        
        // Handle null values for MongoDB query
        String search = filter.getSearch() != null ? filter.getSearch() : "";
        String category = filter.getCategory() != null ? filter.getCategory() : "";
        BigDecimal minPrice = filter.getMinPrice() != null ? filter.getMinPrice() : BigDecimal.ZERO;
        BigDecimal maxPrice = filter.getMaxPrice() != null ? filter.getMaxPrice() : new BigDecimal("999999.99");
        
        Page<Product> page;
        
        // Use different query based on whether attribute filtering is needed
        if (filter.getAttributeName() != null && filter.getAttributeValue() != null) {
            String attributeName = filter.getAttributeName();
            String attributeValue = filter.getAttributeValue();
            page = productRepository.findByFiltersWithAttributes(search, category, minPrice, maxPrice, attributeName, attributeValue, pageable);
        } else {
            page = productRepository.findByFilters(search, category, minPrice, maxPrice, pageable);
        }
        
        // Convert to ProductDetailResponse with stock information
        List<ProductDetailResponse> responses = page.getContent().stream()
                .map(product -> {
                    boolean inStock = CheckUtil.checkInStock(product, inventoryClient, log);
                    return new ProductDetailResponse(
                        product.getProductId(),
                        product.getName(),
                        product.getDescription(),
                        null, // No image field
                        product.getBasePrice().doubleValue(),
                        product.getCategoryName(),
                        inStock
                    );
                }).toList();
        
        return new PageImpl<>(responses, pageable, page.getTotalElements());
    }

    /**
     * Creates a new product in the catalog.
     *
     * @param product The product to create
     * @return The created product with generated ID
     */
    public Product createProduct(Product product) {
        Product saved = productRepository.save(product);
        log.info("Created product with id={}", saved.getProductId());
        return saved;
    }

    /**
     * Updates an existing product's core details.
     * Only updates non-null fields from the provided product data.
     *
     * @param productId The unique identifier of the product to update
     * @param updated The updated product data
     * @return Optional containing the updated product if found, empty otherwise
     */
    public Optional<Product> updateProduct(String productId, Product updated) {
        return productRepository.findById(productId).map(existing -> {
            existing.setName(updated.getName());
            existing.setDescription(updated.getDescription());
            existing.setCategoryName(updated.getCategoryName());
            existing.setBasePrice(updated.getBasePrice());
            existing.setPublished(updated.isPublished());
            Product saved = productRepository.save(existing);
            log.info("Updated product with id={}", productId);
            return saved;
        });
    }

    /**
     * Adds a new variant to an existing product.
     * If the product has no variants, creates a new list.
     *
     * @param productId The unique identifier of the product
     * @param variant The variant to add
     * @return Optional containing the updated product if found, empty otherwise
     */
    public Optional<Product> addVariant(String productId, Variant variant) {
        return productRepository.findById(productId).map(product -> {
            if (product.getVariants() != null) {
                product.getVariants().add(variant);
            } else {
                product.setVariants(List.of(variant));
            }
            Product saved = productRepository.save(product);
            log.info("Added variant to product id={}", productId);
            return saved;
        });
    }

    /**
     * Updates a specific variant of a product by its index.
     * Validates that the variant index is within bounds.
     *
     * @param productId The unique identifier of the product
     * @param variantIndex The index of the variant to update (0-based)
     * @param updatedVariant The updated variant data
     * @return Optional containing the updated product if found and index valid, empty otherwise
     */
    public Optional<Product> updateVariant(String productId, int variantIndex, Variant updatedVariant) {
        return productRepository.findById(productId).map(product -> {
            if (product.getVariants() != null && variantIndex >= 0 && variantIndex < product.getVariants().size()) {
                product.getVariants().set(variantIndex, updatedVariant);
                Product saved = productRepository.save(product);
                log.info("Updated variant index={} for product id={}", variantIndex, productId);
                return saved;
            }
            return product;
        });
    }

    /**
     * Retrieves cached trending products from Redis.
     * Handles both List<Product> and List<LinkedHashMap> data types for backward compatibility.
     *
     * @return List of ProductTrendingResponse from cache
     */
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
                        p.getProductId(),
                        p.getName(),
                        null, // No image field in Product
                        p.getBasePrice().doubleValue()
                ))
                .toList();
    }

}
