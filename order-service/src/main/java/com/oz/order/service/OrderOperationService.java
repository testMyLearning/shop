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
        orderRepository.deleteOrderById(orderId);
            log.warn("Заказ {} отменен. Причина: {}", orderId, reason);

        };

    @Transactional
    public void updateOrderStatus(UUID orderId, String status) {
        orderRepository.updateStatus(orderId, OrderStatus.valueOf(status));
    }
}
