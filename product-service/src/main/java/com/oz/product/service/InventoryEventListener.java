package com.oz.product.service;

import com.oz.common.dto.InventoryFailedEvent;
import com.oz.common.dto.InventoryReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryEventListener {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSuccess(InventoryReservedEvent event) {
        kafkaTemplate.send("inventory-reserved",event.id().toString(), event);
    }

     // Если транзакция упала
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) // Или если мы сами решили отменить
    public void handleFailure(InventoryFailedEvent event) {
        kafkaTemplate.send("inventory-failed", event);
    }
}