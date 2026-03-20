package com.oz.order.decorator;

import com.oz.order.dto.Order;
import com.oz.common.dto.OrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggingOrderDecorator implements OrderProcessor{
    private final OrderProcessor delegate;

    public LoggingOrderDecorator(@Qualifier("simpleCreateOrderProcessor")OrderProcessor delegate) {
        this.delegate = delegate;
    }

    @Override
    public Order process(String userId, OrderRequest request) {
log.info("Начало создания заказа");
Order order = delegate.process(userId,request);
log.info("Заказ создан");
return order;
    }
}
