package com.dtit.product.controllers.responses;

public record ProductCardResponse(
        String id,
        String name,
        String image,
        double price
) {}
