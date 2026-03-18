package com.oz.order.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderCreatedInternalEvent(UUID orderId, String userId, String productId, int quantity, BigDecimal price) {
}
