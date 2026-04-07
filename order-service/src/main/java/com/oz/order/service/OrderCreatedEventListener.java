package com.oz.order.service;

import com.oz.common.dto.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCreatedEventListener {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final OrderOperationService orderService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Транзакция завершена для события: {}", event.orderId());

        kafkaTemplate.send("order-created",event.orderId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Доставлено в топик: {}, партиция: {}, офсет: {}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    } else {
                        log.error("Failed to send Kafka message for order {}", event.orderId(), ex);
                        orderService.cancelOrder(event.orderId(),"OUT_OF_STOCK");

                    }
                });
    }
}