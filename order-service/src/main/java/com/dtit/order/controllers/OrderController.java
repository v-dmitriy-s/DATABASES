package com.dtit.order.controllers;

import com.dtit.order.controllers.requests.PlaceOrderRequest;
import com.dtit.order.controllers.responses.OrderResponse;
import com.dtit.order.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("orders")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    public OrderResponse placeOrder(@RequestBody PlaceOrderRequest request,
                                    @RequestHeader("X-User-Email") String email) {
        return orderService.placeOrder(request, email);
    }

    @GetMapping
    public List<OrderResponse> getOrders(@RequestHeader("X-User-Email") String email) {
        return orderService.getAllOrdersByEmail(email);
    }
}
