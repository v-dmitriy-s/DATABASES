package com.dtit.product.controllers;

import com.dtit.product.domain.Category;
import com.dtit.product.services.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private CategoryService categoryService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllCategories() throws Exception {
        Category cat = new Category();
        cat.setCategoryId("cat-1");
        cat.setName("TestCat");
        Mockito.when(categoryService.getAllCategories()).thenReturn(List.of(cat));
        mockMvc.perform(MockMvcRequestBuilders.get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoryId").value("cat-1"));
    }

    @Test
    void createCategory() throws Exception {
        Category cat = new Category();
        cat.setCategoryId("cat-1");
        cat.setName("TestCat");
        Mockito.when(categoryService.createCategory(any(Category.class))).thenReturn(cat);
        mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cat)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value("cat-1"));
    }

    @Test
    void updateCategory_found() throws Exception {
        Category cat = new Category();
        cat.setCategoryId("cat-1");
        cat.setName("Updated");
        Mockito.when(categoryService.updateCategory(eq("cat-1"), any(Category.class))).thenReturn(Optional.of(cat));
        mockMvc.perform(MockMvcRequestBuilders.put("/categories/cat-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cat)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void updateCategory_notFound() throws Exception {
        Category cat = new Category();
        Mockito.when(categoryService.updateCategory(eq("cat-2"), any(Category.class))).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.put("/categories/cat-2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cat)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCategory() throws Exception {
        Mockito.doNothing().when(categoryService).deleteCategory("cat-1");
        mockMvc.perform(MockMvcRequestBuilders.delete("/categories/cat-1"))
                .andExpect(status().isNoContent());
    }
} 