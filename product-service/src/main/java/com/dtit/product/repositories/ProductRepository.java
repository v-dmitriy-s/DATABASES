package com.dtit.product.repositories;

import com.dtit.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findTop10ByOrderByPriceDesc();
    List<Product> findByCategory(String category);
}
