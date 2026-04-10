package com.oz.common.dto;

import java.util.UUID;

public record InventoryFailedEvent (
        UUID orderId,
        UUID productId,
        int quantity,
        String cause
) {

}
