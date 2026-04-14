package com.oz.cart.mapper;

import com.oz.cart.dto.ResponseCart;
import com.oz.cart.entity.Cart;
import com.oz.product.dto.ProductDto;
import com.oz.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring") // Чтобы маппер был бином и его можно было заинжектить
public interface CartMapper {

    @Mapping(target = "productNamesAndCounts", source = "list")
    @Mapping(target = "totalPrice", source = "list")
    ResponseCart toResponse(Cart cart);

    // Логика превращения списка в Map (Имя -> Количество)
    default Map<String, Long> mapListToNamesAndCounts(List<ProductDto> list) {
        if (list == null) return Map.of();
        return list.stream()
                .collect(Collectors.toMap(
                        ProductDto::name,
                        ProductDto::count,
                        Long::sum // Если вдруг в списке будут дубликаты имен — суммируем
                ));
    }

    // Логика расчета общей суммы
    default BigDecimal calculateTotalPrice(List<ProductDto> list) {
        if (list == null) return BigDecimal.ZERO;
        return list.stream()
                .map(p -> p.price().multiply(BigDecimal.valueOf(p.count())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    default ProductDto toProductDto(Product product, Long count) {
        return new ProductDto(
                product.getName(),
                count,
                product.getPrice(),
                product.getColor(),
                product.getTypeOfThing().getName()
        );
    }
}