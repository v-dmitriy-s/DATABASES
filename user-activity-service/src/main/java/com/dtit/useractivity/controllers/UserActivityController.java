package com.dtit.useractivity.controllers;

import com.dtit.useractivity.controllers.requests.ProductViewRequest;
import com.dtit.useractivity.controllers.responses.ProductCardResponse;
import com.dtit.useractivity.services.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserActivityController {

    private final UserActivityService activityService;

    @GetMapping("/recently-viewed")
    public List<ProductCardResponse> getRecentlyViewed(@RequestHeader("X-User-Email") String email) {
        return activityService.getRecentlyViewed(email);
    }

    @PostMapping("/view")
    public void recordView(@RequestHeader("X-User-Email") String email,
                           @RequestBody ProductViewRequest request) {
        activityService.saveView(email, request.productId());
    }
}

