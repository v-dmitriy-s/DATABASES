package com.dtit.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data Transfer Object for product search and filtering parameters.
 * Encapsulates all search criteria for the product search functionality.
 */
@Data
@Schema(description = "Product search and filter criteria")
public class ProductFilter {
    
    @Schema(description = "Search term for product name/description", example = "headphones")
    private String search;
    
    @Schema(description = "Category name filter", example = "Electronics")
    private String category;
    
    @Schema(description = "Minimum price filter", example = "10.00")
    private BigDecimal minPrice;
    
    @Schema(description = "Maximum price filter", example = "500.00")
    private BigDecimal maxPrice;
    
    @Schema(description = "Attribute name to filter by", example = "Color")
    private String attributeName;
    
    @Schema(description = "Attribute value to filter by", example = "Red")
    private String attributeValue;
    
    @Schema(description = "Page number (0-based)", example = "0")
    private Integer page = 0;
    
    @Schema(description = "Page size", example = "20")
    private Integer size = 10;
} 
