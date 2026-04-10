package com.oz.order.service;

import com.oz.common.chain.OrderValidator;
import com.oz.common.exception.CustomException;
import com.oz.common.executors.CustomThreadPoolExecutor;
import com.oz.common.patterns.requestScope.UserContext;
import com.oz.order.decorator.OrderProcessor;
import com.oz.order.dto.Order;
import com.oz.common.dto.OrderCreatedEvent;
import com.oz.common.dto.OrderRequest;
import com.oz.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


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
    public UUID createOrder(String keycloakId, OrderRequest request) {
        validators.forEach(validators -> validators.validate(keycloakId, request));
        Order savedOrder = orderProcessor.process(keycloakId, request);
            // 2. Отправляем событие для начала саги
            eventPublisher.publishEvent(new OrderCreatedEvent(
                    savedOrder.getId(),
                    keycloakId,
                    request.productId(),
                    request.quantity(),
                    request.price()
            ));
return savedOrder.getId();
    }




    public Order findById(UUID uuid) {
        return orderRepository.findByIdWithStatusCompleted(uuid)
                .orElseThrow(()->new CustomException("ошибка в поиске заказа со статусом completed"));
    }
}
