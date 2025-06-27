package com.dtit.product.services;

import com.dtit.product.domain.Product;
import com.dtit.product.domain.Variant;
import com.dtit.product.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() {
        Product product = new Product();
        product.setProductId("1");
        product.setName("Test Product");
        product.setCategoryName("TestCat");
        product.setBasePrice(new BigDecimal("10.00"));
        product.setPublished(true);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        Product saved = productService.createProduct(product);
        assertEquals("1", saved.getProductId());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct() {
        Product existing = new Product();
        existing.setProductId("1");
        Product updated = new Product();
        updated.setName("Updated");
        updated.setDescription("Desc");
        updated.setCategoryName("Cat");
        updated.setBasePrice(new BigDecimal("20.00"));
        updated.setPublished(false);
        when(productRepository.findById("1")).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenReturn(existing);
        Optional<Product> result = productService.updateProduct("1", updated);
        assertTrue(result.isPresent());
        assertEquals("Updated", result.get().getName());
        verify(productRepository, times(1)).save(existing);
    }

    @Test
    void testAddVariant() {
        Product product = new Product();
        product.setProductId("1");
        when(productRepository.findById("1")).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        Variant variant = new Variant();
        variant.setSku("SKU-1");
        Optional<Product> result = productService.addVariant("1", variant);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getVariants());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateVariant() {
        Product product = new Product();
        product.setProductId("1");
        Variant v1 = new Variant();
        v1.setSku("SKU-1");
        product.setVariants(List.of(v1));
        when(productRepository.findById("1")).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        Variant updated = new Variant();
        updated.setSku("SKU-2");
        Optional<Product> result = productService.updateVariant("1", 0, updated);
        assertTrue(result.isPresent());
        assertEquals("SKU-2", result.get().getVariants().get(0).getSku());
        verify(productRepository, times(1)).save(product);
    }
} 