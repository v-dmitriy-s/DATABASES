package com.dtit.recommendation.services;

import com.dtit.recommendation.clients.ProductClient;
import com.dtit.recommendation.controllers.responses.ProductDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IndexingService {
    private final VectorStore vectorStore;
    private final ProductClient productClient;

    public void init() {
        List<ProductDetailResponse> products = productClient.getAllProducts();

        List<Document> documents = products.stream()
                .map(p -> Document.builder()
                        .id(p.id())
                        .text(p.name() + " " + p.description())
                        .metadata(Map.of(
                                "name", p.name(),
                                "description", p.description(),
                                "image", p.image(),
                                "price", p.price(),
                                "category", p.category()
                        ))
                        .build()).toList();

        vectorStore.add(documents);
    }
}
