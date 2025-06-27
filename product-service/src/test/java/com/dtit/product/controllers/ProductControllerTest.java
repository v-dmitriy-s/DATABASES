package com.dtit.product.controllers;

import com.dtit.product.domain.Product;
import com.dtit.product.domain.Variant;
import com.dtit.product.services.ProductService;
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

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private ProductService productService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getProductDetail_found() throws Exception {
        Product product = new Product();
        product.setProductId("1");
        product.setName("Test Product");
        Mockito.when(productService.getProductById("1")).thenReturn(Optional.of(product));
        mockMvc.perform(MockMvcRequestBuilders.get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value("1"));
    }

    @Test
    void getProductDetail_notFound() throws Exception {
        Mockito.when(productService.getProductById("2")).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/products/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProduct() throws Exception {
        Product product = new Product();
        product.setProductId("1");
        product.setName("Test Product");
        product.setCategoryName("Cat");
        product.setBasePrice(new BigDecimal("10.00"));
        product.setPublished(true);
        Mockito.when(productService.createProduct(any(Product.class))).thenReturn(product);
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value("1"));
    }

    @Test
    void updateProduct_found() throws Exception {
        Product product = new Product();
        product.setProductId("1");
        product.setName("Updated");
        Mockito.when(productService.updateProduct(eq("1"), any(Product.class))).thenReturn(Optional.of(product));
        mockMvc.perform(MockMvcRequestBuilders.put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void updateProduct_notFound() throws Exception {
        Product product = new Product();
        Mockito.when(productService.updateProduct(eq("2"), any(Product.class))).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.put("/products/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isNotFound());
    }

    @Test
    void addVariant() throws Exception {
        Product product = new Product();
        product.setProductId("1");
        Variant variant = new Variant();
        variant.setSku("SKU-1");
        Mockito.when(productService.addVariant(eq("1"), any(Variant.class))).thenReturn(Optional.of(product));
        mockMvc.perform(MockMvcRequestBuilders.post("/products/1/variants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(variant)))
                .andExpect(status().isOk());
    }

    @Test
    void updateVariant() throws Exception {
        Product product = new Product();
        product.setProductId("1");
        Variant variant = new Variant();
        variant.setSku("SKU-2");
        Mockito.when(productService.updateVariant(eq("1"), eq(0), any(Variant.class))).thenReturn(Optional.of(product));
        mockMvc.perform(MockMvcRequestBuilders.put("/products/1/variants/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(variant)))
                .andExpect(status().isOk());
    }
} 