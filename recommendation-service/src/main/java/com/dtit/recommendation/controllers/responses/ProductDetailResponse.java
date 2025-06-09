package com.dtit.recommendation.controllers.responses;

public record ProductDetailResponse(String id,
                                    String name,
                                    String description,
                                    String image,
                                    double price,
                                    String category,
                                    boolean inStock) {
}
