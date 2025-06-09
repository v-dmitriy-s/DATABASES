package com.dtit.useractivity.services;

import com.dtit.useractivity.clients.ProductClient;
import com.dtit.useractivity.controllers.responses.ProductCardResponse;
import com.dtit.useractivity.model.UserActivity;
import com.dtit.useractivity.repositories.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserActivityService {

    private final UserActivityRepository repository;
    private final ProductClient productClient; // HTTP client to Product Service

    public void saveView(String email, String productId) {
        UserActivity activity = new UserActivity(email, Instant.now(), productId);
        repository.save(activity);
    }

    public List<ProductCardResponse> getRecentlyViewed(String email) {
        List<UserActivity> views = repository.findTop10ByUserEmail(email);
        List<String> productIds = views.stream().map(UserActivity::getProductId).toList();
        return productClient.getProducts(productIds);
    }
}
