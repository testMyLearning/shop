package com.oz.payment;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(indexes = @Index(columnList = "orderId", unique=true))
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private UUID orderId;
    private String userId;
    private BigDecimal price;
}
