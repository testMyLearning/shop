package com.oz.order.strategy;

import com.oz.common.dto.InventoryFailedEvent;

public interface CancelledStrategy {

    void handle(InventoryFailedEvent event);


    String getStockErrorCode();
}
