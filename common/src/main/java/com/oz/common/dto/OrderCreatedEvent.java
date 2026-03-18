package com.oz.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;


public record OrderCreatedEvent (
   UUID orderId,
String userId,
  UUID productId,
   Integer quantity,
 BigDecimal totalPrice
)
{}
