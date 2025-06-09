package com.dtit.order.services;

import com.dtit.order.controllers.requests.PlaceOrderRequest;
import com.dtit.order.controllers.responses.OrderResponse;
import com.dtit.order.model.Order;
import com.dtit.order.model.OrderItem;
import com.dtit.order.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request, String email) {
        Order order = new Order();
        order.setUserEmail(email);
        order.setStatus("PLACED");
        order.setPaymentMethod(request.payment());
        order.setShipping(BigDecimal.TEN); // fixed shipping cost
        order.setTrackingNumber("TRK" + System.currentTimeMillis()); // or any generator

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();

        for (PlaceOrderRequest.CartItemDto itemDto : request.items()) {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(itemDto.productId());
            item.setProductName(itemDto.name());
            item.setProductImage(itemDto.image());
            item.setPrice(itemDto.price());
            item.setQuantity(itemDto.quantity());

            total = total.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            items.add(item);
        }

        order.setItems(items);
        order.setTotal(total.add(order.getShipping())); // add shipping to total

        return toResponse(orderRepository.save(order));
    }

    public List<OrderResponse> getAllOrdersByEmail(String email) {
        return orderRepository.findByUserEmail(email)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private OrderResponse toResponse(Order order) {
        List<OrderResponse.OrderItemDto> itemDtos = order.getItems().stream()
                .map(item -> new OrderResponse.OrderItemDto(
                        item.getProductName(),
                        item.getProductImage(),
                        item.getQuantity(),
                        item.getPrice()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getCreatedAt(),
                order.getTotal(),
                order.getStatus(),
                order.getPaymentMethod(),
                order.getTrackingNumber(),
                order.getShipping(),
                itemDtos
        );
    }
}
