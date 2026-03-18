package com.oz.order.service;

import com.oz.order.dto.Order;
import com.oz.order.enums.OrderStatus;
import com.oz.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderOperationService {
    private final OrderRepository orderRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW) // Важно: новая транзакция!
    public void cancelOrder(UUID orderId, String reason) {
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus(OrderStatus.CANCELLED);
            log.warn("Заказ {} отменен. Причина: {}", orderId, reason);
            orderRepository.save(order);
        });
    }
    @Transactional
    public void updateOrderStatus(UUID orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.valueOf(status));
        orderRepository.save(order);
    }
}
