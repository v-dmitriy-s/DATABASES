package com.dtit.product.controllers;

import com.dtit.product.clients.InventoryClient;
import com.dtit.product.clients.InventoryItem;
import com.dtit.product.controllers.responses.ProductCardResponse;
import com.dtit.product.controllers.responses.ProductDetailResponse;
import com.dtit.product.controllers.responses.ProductTrendingResponse;
import com.dtit.product.model.Product;
import com.dtit.product.services.ProductService;
import com.dtit.product.utils.CheckUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequiredArgsConstructor
@RequestMapping("products")
@CrossOrigin(origins = "*")
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final InventoryClient inventoryClient;

    @GetMapping
    public ResponseEntity<List<ProductDetailResponse>> getAllProducts() {
        List<ProductDetailResponse> products = productService.getAllProducts();
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductCardResponse>> getProductsByCategory(@PathVariable String category) {
        List<ProductCardResponse> products = productService.getProductsByCategory(category);
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/trending")
    public ResponseEntity<List<ProductTrendingResponse>> getTrendingProducts() {
        List<ProductTrendingResponse> trending = productService.getTrendingProducts();
        if (trending.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(trending);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getProductDetail(@PathVariable String id) {
        return productService.getProductById(id)
                .map(product -> {
                    boolean inStock = CheckUtil.checkInStock(product, inventoryClient, log);
                    return ResponseEntity.ok(
                            new ProductDetailResponse(
                                    product.getId(),
                                    product.getName(),
                                    product.getDescription(),
                                    product.getImage(),
                                    product.getPrice(),
                                    product.getCategory(),
                                    inStock
                            )
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }



    @PostMapping("/bulk")
    public List<ProductCardResponse> getProducts(@RequestBody List<String> ids) {
        return productService.getProductsByIds(ids);
    }
}
