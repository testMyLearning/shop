package com.oz.common.chain;

import com.oz.common.dto.OrderRequest;

public interface OrderValidator {
    void validate(String userId, OrderRequest orderRequest);
}
