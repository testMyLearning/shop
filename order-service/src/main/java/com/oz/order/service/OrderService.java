package com.oz.order.service;

import com.oz.order.dto.Order;
import com.oz.common.dto.OrderCreatedEvent;
import com.oz.order.dto.OrderRequest;
import com.oz.order.enums.OrderStatus;
import com.oz.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Order createOrder(String userId, OrderRequest request) {
        // 1. Создаем заказ в статусе PENDING
        Order order=createOrderFromRequest(userId ,request);

        Order savedOrder = orderRepository.save(order);
        log.info("Order created with ID: {}", savedOrder.getId());

        // 2. Отправляем событие для начала саги
        eventPublisher.publishEvent(new OrderCreatedEvent(
                savedOrder.getId(),
                userId,
                request.productId(),
                request.quantity(),
                request.price()
        ));

        return savedOrder;
    }

    private Order createOrderFromRequest(String userId, OrderRequest request){
        Order order = new Order();
        order.setUserId(userId);
        order.setProductId(request.productId());
        order.setQuantity(request.quantity());
        order.setTotalPrice(request.price());
        order.setStatus(OrderStatus.PENDING);
        return order;
    }

}
