package com.oz.order.decorator;

import com.oz.order.dto.Order;
import com.oz.common.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TimeOrderDecorator implements OrderProcessor
{
    @Qualifier("loggingOrderDecorator")
    private final OrderProcessor orderProcessor;

    @Override
    public Order process(String userId, OrderRequest request) {
        long startTime = System.currentTimeMillis(); // Засекли старт

        try {
            // Передаем управление дальше по цепочке (Логированию -> Базе)
            return orderProcessor.process(userId, request);
        } finally {
            long endTime = System.currentTimeMillis(); // Засекли финиш
            log.info("Полное время создания заказа: {} мс", (endTime - startTime));
        }
    }
    }

