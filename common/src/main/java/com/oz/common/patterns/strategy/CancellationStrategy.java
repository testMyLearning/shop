package com.oz.common.patterns.strategy;

import com.oz.common.dto.InventoryFailedEvent;

public interface CancellationStrategy {

    void handle(InventoryFailedEvent event);

    String getStockErrorCode();
}
