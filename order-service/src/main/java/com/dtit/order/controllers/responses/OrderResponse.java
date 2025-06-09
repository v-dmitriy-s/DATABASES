package com.dtit.order.controllers.responses;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        Instant createdAt,
        BigDecimal total,
        String status,
        String paymentMethod,
        String trackingNumber,
        BigDecimal shipping,
        List<OrderItemDto> items
) {
    public record OrderItemDto(
            String name,
            String image,
            int quantity,
            BigDecimal price
    ) {}
}
