package com.oz.order.service;

import com.oz.common.chain.OrderValidator;
import com.oz.common.patterns.requestScope.UserContext;
import com.oz.order.decorator.OrderProcessor;
import com.oz.order.dto.Order;
import com.oz.common.dto.OrderCreatedEvent;
import com.oz.common.dto.OrderRequest;
import com.oz.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final OrderProcessor orderProcessor;
    private final List<OrderValidator> validators;
    //private final UserContext userContext;
    // Можно реализовать через прокидывание контекста с данными изи JWT и использовать Например userContext.getUserId()

    @Transactional
    public Order createOrder(String userId, OrderRequest request) {
        validators.forEach(validators-> validators.validate(userId,request));
        Order savedOrder = orderProcessor.process(userId,request);
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



    public Order findById(UUID uuid) {
        return orderRepository.findByIdWithStatusCompleted(uuid)
                .orElseThrow(()->new RuntimeException("ошибка в поиске заказа со статусом completed"));
    }
}
