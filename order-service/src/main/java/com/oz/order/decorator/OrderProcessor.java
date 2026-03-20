package com.oz.order.decorator;

import com.oz.order.dto.Order;
import com.oz.common.dto.OrderRequest;

public interface OrderProcessor {
    Order process(String userId, OrderRequest request);
}
