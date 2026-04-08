package com.oz.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SliceResponse<T> {
    private List<T> responseBody;
    private int page;
    private int size;
    private boolean hasNext;
}
