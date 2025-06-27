package com.dtit.product.controllers;

import com.dtit.product.controllers.responses.ProductDetailResponse;
import com.dtit.product.controllers.responses.ProductTrendingResponse;
import com.dtit.product.domain.Product;
import com.dtit.product.domain.Variant;
import com.dtit.product.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.dtit.product.dto.ProductFilter;

import java.util.List;

/**
 * REST controller for managing products and their variants.
 * Provides endpoints for product search, detail, creation, update, and variant management.
 */
@Tag(name = "Products", description = "Product and variant management APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
@CrossOrigin(origins = "*")
@Slf4j
public class ProductController {

    private final ProductService productService;

    /**
     * List/Search Products (paginated, filterable by category, price, and attributes).
     */
    @Operation(summary = "List/Search Products", description = "Retrieve a paginated list of products, with capabilities for searching and filtering by category, price, and attributes.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paginated list of products returned"),
        @ApiResponse(responseCode = "204", description = "No products found")
    })
    @GetMapping
    public ResponseEntity<Page<ProductDetailResponse>> getProducts(@ModelAttribute ProductFilter filter ) {
        Page<ProductDetailResponse> products = productService.searchProducts(filter);
        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
    }

    /**
     * Get Product Details by ID.
     */
    @Operation(summary = "Get Product Details", description = "Retrieve all public information for a single product, including its available variants and their attributes.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product details returned"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductDetail(
            @Parameter(description = "Product identifier") @PathVariable String productId) {
        return productService.getProductById(productId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    /**
     * Create a new product.
     */
    @Operation(summary = "Create Product", description = "Add a new base product to the catalog.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product created successfully")
    })
    @PostMapping
    public ResponseEntity<Product> createProduct(
            @Parameter(description = "Product to create") @RequestBody Product product) {
        Product created = productService.createProduct(product);
        return ResponseEntity.ok(created);
    }

    /**
     * Update an existing product.
     */
    @Operation(summary = "Update Product", description = "Modify the core details of a product.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "Product identifier") @PathVariable String productId,
            @Parameter(description = "Updated product data") @RequestBody Product product) {
        return productService.updateProduct(productId, product)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    @GetMapping("/trending")
    public ResponseEntity<List<ProductTrendingResponse>> getTrendingProducts() {
        List<ProductTrendingResponse> trending = productService.getTrendingProducts();
        if (trending.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(trending);
    }

    /**
     * Add a new variant to a product.
     */
    @Operation(summary = "Add Variant", description = "Add a new variant (e.g., a new size/color combination) to an existing product.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Variant added successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping("/{productId}/variants")
    public ResponseEntity<Product> addVariant(
            @Parameter(description = "Product identifier") @PathVariable String productId,
            @Parameter(description = "Variant to add") @RequestBody Variant variant) {
        return productService.addVariant(productId, variant)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    /**
     * Update a variant of a product by index.
     */
    @Operation(summary = "Update Variant", description = "Modify the details of a specific variant (e.g., change its price or SKU).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Variant updated successfully"),
        @ApiResponse(responseCode = "404", description = "Product or variant not found")
    })
    @PutMapping("/{productId}/variants/{variantIndex}")
    public ResponseEntity<Product> updateVariant(
            @Parameter(description = "Product identifier") @PathVariable String productId,
            @Parameter(description = "Index of the variant in the product's variant list") @PathVariable int variantIndex,
            @Parameter(description = "Updated variant data") @RequestBody Variant variant) {
        return productService.updateVariant(productId, variantIndex, variant)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product or variant not found"));
    }

}
