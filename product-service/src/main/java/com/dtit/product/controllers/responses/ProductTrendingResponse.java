package com.dtit.product.controllers.responses;

public record ProductTrendingResponse(
        String id,
        String name,
        String image,
        double price
) {}
