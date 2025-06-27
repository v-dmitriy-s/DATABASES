package com.dtit.product.services;

import com.dtit.product.domain.Category;
import com.dtit.product.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing product categories and their hierarchy.
 * Provides business logic for category CRUD operations and hierarchical structure management.
 * Supports nested categories for organizing products in a tree-like structure.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;

    /**
     * Retrieves all categories from the database.
     * Returns the complete category hierarchy including subcategories.
     *
     * @return List of all categories with their subcategories
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Retrieves a category by its unique identifier.
     *
     * @param id The unique identifier of the category
     * @return Optional containing the category if found, empty otherwise
     */
    public Optional<Category> getCategoryById(String id) {
        return categoryRepository.findById(id);
    }

    /**
     * Creates a new category in the catalog.
     * Supports hierarchical structure with subcategories.
     *
     * @param category The category to create
     * @return The created category with generated ID
     */
    public Category createCategory(Category category) {
        Category saved = categoryRepository.save(category);
        log.info("Created category with id={}", saved.getCategoryId());
        return saved;
    }

    /**
     * Updates an existing category's details.
     * Updates name, description, and subcategories structure.
     *
     * @param categoryId The unique identifier of the category to update
     * @param updated The updated category data
     * @return Optional containing the updated category if found, empty otherwise
     */
    public Optional<Category> updateCategory(String categoryId, Category updated) {
        return categoryRepository.findById(categoryId).map(existing -> {
            existing.setName(updated.getName());
            existing.setDescription(updated.getDescription());
            existing.setSubCategories(updated.getSubCategories());
            Category saved = categoryRepository.save(existing);
            log.info("Updated category with id={}", categoryId);
            return saved;
        });
    }

    /**
     * Deletes a category by its unique identifier.
     * Removes the category and all its subcategories from the database.
     *
     * @param categoryId The unique identifier of the category to delete
     */
    public void deleteCategory(String categoryId) {
        categoryRepository.deleteById(categoryId);
        log.info("Deleted category with id={}", categoryId);
    }
} 