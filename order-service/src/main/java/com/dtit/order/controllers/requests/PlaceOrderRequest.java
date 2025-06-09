package com.dtit.order.controllers.requests;

import com.dtit.order.model.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public record PlaceOrderRequest(
        String address,
        String payment,
        List<CartItemDto> items
) {
    public record CartItemDto(
            Long productId,
            String name,
            String image,
            BigDecimal price,
            int quantity
    ) {}
}
