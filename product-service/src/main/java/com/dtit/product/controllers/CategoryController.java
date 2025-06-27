package com.dtit.product.controllers;

import com.dtit.product.domain.Category;
import com.dtit.product.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

/**
 * REST controller for managing product categories and their hierarchy.
 * Provides endpoints for category listing, creation, update, and deletion.
 */
@Tag(name = "Categories", description = "Product category management APIs")
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * List all categories (with hierarchy).
     */
    @Operation(summary = "List Categories", description = "Retrieve the hierarchy of all available product categories.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of categories returned"),
        @ApiResponse(responseCode = "204", description = "No categories found")
    })
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return categories.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(categories);
    }

    /**
     * Create a new category.
     */
    @Operation(summary = "Create Category", description = "Add a new product category.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category created successfully")
    })
    @PostMapping
    public ResponseEntity<Category> createCategory(
            @Parameter(description = "Category to create") @RequestBody Category category) {
        Category created = categoryService.createCategory(category);
        return ResponseEntity.ok(created);
    }

    /**
     * Update an existing category.
     */
    @Operation(summary = "Update Category", description = "Modify the details of a product category.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category updated successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(
            @Parameter(description = "Category identifier") @PathVariable String categoryId,
            @Parameter(description = "Updated category data") @RequestBody Category category) {
        return categoryService.updateCategory(categoryId, category)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    /**
     * Delete a category by ID.
     */
    @Operation(summary = "Delete Category", description = "Delete a product category by its ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Category deleted successfully")
    })
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "Category identifier") @PathVariable String categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
} 