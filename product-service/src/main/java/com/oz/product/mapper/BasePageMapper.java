package com.oz.product.mapper;

import com.oz.common.dto.PageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;


@MapperConfig
public interface BasePageMapper<E,D> {
    @Mapping(target = "totalPages", source = "totalPages")
    @Mapping(target = "totalElements", source = "totalElements")
    @Mapping(target = "size", source = "size")
    @Mapping(target = "lastPage", source = "last")
    @Mapping(target = "firstPage", source = "first")
    @Mapping(target = "page", source = "number")
    @Mapping(target = "responseBody", source = "content")
    PageResponse<D> toPageResponse(Page<E> page);
}
