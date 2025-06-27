package com.dtit.product.services;

import com.dtit.product.domain.Category;
import com.dtit.product.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCategory() {
        Category category = new Category();
        category.setCategoryId("cat-1");
        category.setName("TestCat");
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        Category saved = categoryService.createCategory(category);
        assertEquals("cat-1", saved.getCategoryId());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testUpdateCategory() {
        Category existing = new Category();
        existing.setCategoryId("cat-1");
        Category updated = new Category();
        updated.setName("Updated");
        updated.setDescription("Desc");
        when(categoryRepository.findById("cat-1")).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(Category.class))).thenReturn(existing);
        Optional<Category> result = categoryService.updateCategory("cat-1", updated);
        assertTrue(result.isPresent());
        assertEquals("Updated", result.get().getName());
        verify(categoryRepository, times(1)).save(existing);
    }

    @Test
    void testDeleteCategory() {
        doNothing().when(categoryRepository).deleteById("cat-1");
        categoryService.deleteCategory("cat-1");
        verify(categoryRepository, times(1)).deleteById("cat-1");
    }
} 