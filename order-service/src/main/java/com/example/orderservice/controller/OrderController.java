package com.example.orderservice.controller;

import com.example.orderservice.client.UserClient;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserClient userClient;
    private final OrderService orderService;

    @PostMapping
    public Order placeOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }
    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        var user = userClient.getUserById(order.getUserId());
        return OrderResponse.builder()
                .orderId(order.getId())
                .product(order.getProduct())
                .price(order.getPrice())
                .user(user)
                .build();
    }
    @GetMapping
    public List<OrderResponse> getAllOrders() {
        var orders = orderRepository.findAll();
        var result = new ArrayList<OrderResponse>();
        for (var order : orders) {
            var user = userClient.getUserById(order.getUserId());
            var response = OrderResponse.builder()
                    .orderId(order.getId())
                    .product(order.getProduct())
                    .price(order.getPrice())
                    .user(user)
                    .build();
            result.add(response);
        }
        return result;
    }
}
