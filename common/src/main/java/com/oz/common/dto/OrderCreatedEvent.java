package com.oz.common.dto;

import java.math.BigDecimal;
import java.util.UUID;


public record OrderCreatedEvent (
   UUID orderId,
String keycloakId,
  UUID productId,
   Integer quantity,
 BigDecimal totalPrice
)
{}
