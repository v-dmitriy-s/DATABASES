package com.dtit.product.clients;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange("/api/inventory")
public interface InventoryClient {

    @GetExchange("/{productId}")
    List<InventoryItem> getStockByProduct(@PathVariable String productId);
}
