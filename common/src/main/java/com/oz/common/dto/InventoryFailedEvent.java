package com.oz.common.dto;

import java.util.UUID;

public record InventoryFailedEvent (UUID id, UUID productId, int quantity, String cause) {

}
