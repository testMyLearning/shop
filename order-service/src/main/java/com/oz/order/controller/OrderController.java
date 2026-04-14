package com.oz.order.controller;

import com.oz.common.dto.OrderRequest;
import com.oz.order.dto.Order;
import com.oz.order.service.FutureService;
import com.oz.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final FutureService futureService;


    @PostMapping
//    @PreAuthorize("hasRole('buyer')")
    public ResponseEntity<String> createOrder(
            @Valid @RequestBody OrderRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        String keycloakId = jwt.getSubject();
        CompletableFuture<ResponseEntity<String>> future = new CompletableFuture<>();
        UUID orderId = orderService.testSemaphore(keycloakId, request);
        futureService.register(orderId,future);
        try {
            return future.get(7, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            return ResponseEntity.status(408).body("Timeout");
        } catch (ExecutionException e) {
            throw new RuntimeException("Ошибка выполнения контроллера на создание заказа");
        } catch (InterruptedException e) {
            throw new RuntimeException("Ошибка прерывания метода гет в контроллере");
        } finally {
            futureService.remove(orderId);
        }
    }

    @GetMapping("/status/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable("id")UUID uuid){
    Order order = orderService.findById(uuid);
    return ResponseEntity.ok(order);
    }

//
//    @GetMapping("/{orderId}")
//    @PreAuthorize("hasAnyRole('buyer', 'seller', 'admin')")
//    public ResponseEntity<OrderResponse> getOrder(@PathVariable UUID orderId) {
//        return ResponseEntity.ok(orderService.getOrder(orderId));
//    }
}
