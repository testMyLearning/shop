package com.oz.order.strategy;

import com.oz.common.dto.InventoryFailedEvent;
import com.oz.order.enums.OrderStatus;
import com.oz.order.repository.OrderRepository;
import jakarta.persistence.CollectionTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RollbackPaymentStrategy implements CancelledStrategy{
    private final OrderRepository orderRepository;

    @Override
    public void handle(InventoryFailedEvent event) {
        log.info("Ошибка оплаты, проставляем статус отменен");
        orderRepository.updateStatus(event.id(), OrderStatus.CANCELLED);
        log.error("Ошибка при заказе товара с id: {} ",event.productId());
    }


    @Override
    public String getStockErrorCode() {
        return "ERROR_PAYMENT";
    }
}

