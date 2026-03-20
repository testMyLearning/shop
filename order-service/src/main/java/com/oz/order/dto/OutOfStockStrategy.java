package com.oz.order.dto;

import com.oz.common.dto.InventoryFailedEvent;
import com.oz.common.patterns.strategy.CancellationStrategy;
import com.oz.order.enums.OrderStatus;
import com.oz.order.repository.OrderRepository;
import com.oz.order.service.OrderOperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutOfStockStrategy implements CancellationStrategy {
    private final OrderRepository orderRepository;

    @Override
    public void handle(InventoryFailedEvent event) {
        System.out.println("Ошибка из паттерна стратегии об отсутствующем товаре");
        orderRepository.updateStatus(event.id(), OrderStatus.ERROR_OUT_OF_STOCKS);
        log.error("Ошибка при заказе товара с id: {} ",event.productId());
    }


    @Override
    public String getStockErrorCode() {
        return "OUT_OF_STOCK";
    }
}
