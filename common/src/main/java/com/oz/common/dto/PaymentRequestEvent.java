package com.oz.common.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequestEvent(UUID orderId, String userId, BigDecimal price,UUID productId,int quantity) {
}
