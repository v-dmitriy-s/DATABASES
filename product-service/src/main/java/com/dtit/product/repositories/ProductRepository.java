package com.dtit.product.repositories;

import com.dtit.product.domain.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findTop10ByOrderByBasePriceDesc();
    List<Product> findByCategoryName(String categoryName);
    List<Product> findByIsPublished(boolean isPublished);
    Page<Product> findByNameContainingIgnoreCaseAndCategoryNameContainingIgnoreCase(String name, String categoryName, Pageable pageable);
    
    @Query("{'name': {$regex: ?0, $options: 'i'}, 'categoryName': {$regex: ?1, $options: 'i'}, 'basePrice': {$gte: ?2, $lte: ?3}}")
    Page<Product> findByFilters(String search, String category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    @Query("{'name': {$regex: ?0, $options: 'i'}, 'categoryName': {$regex: ?1, $options: 'i'}, 'basePrice': {$gte: ?2, $lte: ?3}, 'variants.attributeValues': {$elemMatch: {'name': {$regex: ?4, $options: 'i'}, 'value': {$regex: ?5, $options: 'i'}}}}")
    Page<Product> findByFiltersWithAttributes(String search, String category, BigDecimal minPrice, BigDecimal maxPrice, String attributeName, String attributeValue, Pageable pageable);
}
