package com.dtit.recommendation.services;

import com.dtit.recommendation.clients.ProductClient;
import com.dtit.recommendation.controllers.responses.RecommendationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final VectorStore vectorStore;
    private final ProductClient productClient;

    public List<RecommendationResponse> getRecommendations(String userEmail) {

        SearchRequest query = SearchRequest.builder().query(userEmail).topK(5).build();

        List<Document> results = vectorStore.similaritySearch(query);

        List<String> productIds = results != null ? results.stream()
                .map(doc -> doc.getMetadata().get("product_id").toString())
                .toList() : null;



        return productClient.getProducts(productIds).stream()
                .map(p -> new RecommendationResponse(p.id(), p.name(), p.image(), p.price()))
                .toList();
    }
}

