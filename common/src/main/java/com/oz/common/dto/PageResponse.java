package com.oz.common.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private List<T> responseBody;
    private int page;
    private int size;
    private long totalElements;
    private long totalPages;
    private boolean lastPage;
    private boolean firstPage;
}
