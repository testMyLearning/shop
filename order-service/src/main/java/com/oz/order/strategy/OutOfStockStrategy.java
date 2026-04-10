package com.oz.order.strategy;

import com.oz.common.dto.InventoryFailedEvent;
import com.oz.order.enums.OrderStatus;
import com.oz.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutOfStockStrategy implements CancelledStrategy {
    private final OrderRepository orderRepository;

    @Override
    public void handle(InventoryFailedEvent event) {
        log.info("Ошибка из паттерна стратегии out_of_stock");
        orderRepository.updateStatus(event.orderId(), OrderStatus.ERROR_OUT_OF_STOCKS);
        log.error("Ошибка при заказе товара с orderId: {} ",event.productId());
    }


    @Override
    public String getStockErrorCode() {
        return "OUT_OF_STOCK";
    }
}
