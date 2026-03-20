package com.oz.order.decorator;

import com.oz.order.dto.Order;
import com.oz.common.dto.OrderRequest;
import com.oz.order.enums.OrderStatus;
import com.oz.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Primary
public class SimpleCreateOrderProcessor implements OrderProcessor{
    private final OrderRepository orderRepository;
    @Override
    public Order process(String userId, OrderRequest request) {
        Order order = new Order();
        order.setUserId(userId);
        order.setProductId(request.productId());
        order.setQuantity(request.quantity());
        order.setTotalPrice(request.price());
        order.setStatus(OrderStatus.PENDING);

        return orderRepository.save(order);

    }

}
