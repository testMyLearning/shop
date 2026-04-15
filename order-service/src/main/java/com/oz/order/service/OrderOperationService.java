package com.oz.order.service;

import com.oz.common.chain.OrderValidator;
import com.oz.common.dto.InventoryFailedEvent;
import com.oz.common.dto.OrderCreatedEvent;
import com.oz.common.dto.OrderRequest;
import com.oz.order.decorator.OrderProcessor;
import com.oz.order.strategy.CancelledStrategy;
import com.oz.order.dto.Order;
import com.oz.order.enums.OrderStatus;
import com.oz.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderOperationService {
    private final OrderRepository orderRepository;
    private final Map<String, CancelledStrategy> strategiesMap;
    private final ApplicationEventPublisher eventPublisher;
    private final OrderProcessor orderProcessor;
    private final List<OrderValidator> validators;

    public OrderOperationService(OrderRepository orderRepository, List<CancelledStrategy> strategies,
                                 ApplicationEventPublisher eventPublisher,
                                 OrderProcessor orderProcessor,
                                 List<OrderValidator> validators) {
        this.orderRepository = orderRepository;
        // Группируем стратегии по их внутреннему коду ошибки
        this.strategiesMap = strategies.stream()
                .collect(Collectors.toMap(CancelledStrategy::getStockErrorCode, s -> s));
        this.eventPublisher = eventPublisher;
        this.orderProcessor = orderProcessor;
        this.validators = validators;
    }

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


    @Transactional// Важно: новая транзакция!
    public void cancelOrder(UUID orderId, String reason) {
        log.info("Пришла команда ОТМЕНИТЬ ЗАКАЗ");
        Order order = orderRepository.findById(orderId).orElseThrow();

        CancelledStrategy strategy = strategiesMap.get(reason);
        if (strategy != null) {
            strategy.handle(new InventoryFailedEvent(
                    orderId,
                    order.getProductId(),
                    order.getQuantity(),
                    reason
            ));
            //TODO должна быть логика измненеия статуса заказа или отправки сообщения в очередб
            log.info("Отмена заказа {}. Статус изменён на {}", orderId, strategy.getStockErrorCode());
        } else {
            orderRepository.deleteOrderById(orderId);
            log.warn("Заказ {} отменен. Причина: {}", orderId, reason);
        }
    }

    @Transactional
    public void updateOrderStatus(UUID orderId, OrderStatus status) {
        orderRepository.updateStatus(orderId, status);
    }
}
