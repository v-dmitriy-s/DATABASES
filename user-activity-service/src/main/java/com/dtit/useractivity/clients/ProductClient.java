package com.dtit.useractivity.clients;

import com.dtit.useractivity.controllers.responses.ProductCardResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange("/api/products")
public interface ProductClient {

    @PostExchange("/bulk")
    List<ProductCardResponse> getProducts(@RequestBody List<String> ids);
}
