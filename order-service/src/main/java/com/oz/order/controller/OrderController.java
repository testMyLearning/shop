package com.oz.order.controller;

import com.oz.order.dto.Order;
import com.oz.common.dto.OrderRequest;
import com.oz.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('buyer')")
    public ResponseEntity<Order> createOrder(
            @Valid @RequestBody OrderRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        String userId = jwt.getSubject();

        Order order = orderService.createOrder(userId, request);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable("id")UUID uuid){
    Order order = orderService.findById(uuid);
    return ResponseEntity.ok(order);
    }
//
//    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('buyer', 'seller', 'admin')")
//    public ResponseEntity<OrderResponse> getOrder(@PathVariable UUID id) {
//        return ResponseEntity.ok(orderService.getOrder(id));
//    }
}
