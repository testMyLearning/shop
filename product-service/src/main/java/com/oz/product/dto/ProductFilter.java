package com.oz.product.dto;

import com.oz.common.enums.Color;
import com.oz.product.enums.TypeOfThing;
import jakarta.annotation.Nullable;

import java.math.BigDecimal;

public record ProductFilter(
        @Nullable
        TypeOfThing type,
        @Nullable
        BigDecimal maxPrice,
        @Nullable
        Integer minCount,
        @Nullable
        Color color
) {}
