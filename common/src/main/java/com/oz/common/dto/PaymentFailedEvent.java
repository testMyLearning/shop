package com.oz.common.dto;

import java.util.UUID;

public record PaymentFailedEvent(UUID orderId, UUID productId, int quantity, String reason) {
}
