package com.dtit.recommendation.clients;

import com.dtit.recommendation.controllers.responses.ProductCardResponse;
import com.dtit.recommendation.controllers.responses.ProductDetailResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange("/api/products")
public interface ProductClient {

    @PostExchange("/bulk")
    List<ProductCardResponse> getProducts(@RequestBody List<String> ids);
    @GetExchange
    List<ProductDetailResponse> getAllProducts();
}
