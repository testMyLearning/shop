package com.oz.product.specifications;

import com.oz.common.enums.Color;
import com.oz.product.entity.Product;
import com.oz.product.enums.TypeOfThing;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecifications {

    public static Specification<Product> hasType(TypeOfThing type) {
        return (root, query, cb) ->
                type == null ? null : cb.equal(root.get("typeOfThing"), type);
    }

    public static Specification<Product> priceLessThan(BigDecimal maxPrice) {
        return (root, query, cb) ->
                maxPrice == null ? null : cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Product> hasMinQuantity(Integer minCount) {
        return (root, query, cb) ->
                minCount == null ? null : cb.greaterThanOrEqualTo(root.get("count"), minCount);
    }

    public static Specification<Product> hasColor(Color color) {
        return (root,query ,cb ) ->
        color==null? null : cb.equal(root.get("color"),color);
    }
}