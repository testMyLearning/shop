package com.oz.common.dto;

import java.util.UUID;

public record InventoryReservedEvent(UUID orderId,UUID productId,int quantity)  {

}
