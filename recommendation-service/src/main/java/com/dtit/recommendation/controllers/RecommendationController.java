package com.dtit.recommendation.controllers;

import com.dtit.recommendation.controllers.responses.RecommendationResponse;
import com.dtit.recommendation.services.IndexingService;
import com.dtit.recommendation.services.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("recommendations")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final IndexingService indexingService;

    @GetMapping
    public List<RecommendationResponse> getRecommendedProducts(@RequestHeader("X-User-Email") String userEmail) {
        return recommendationService.getRecommendations(userEmail);
    }

    @GetMapping("/init")
    public void init() {
        indexingService.init();
    }
}
