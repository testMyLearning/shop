package com.oz.product.entity;

import com.oz.common.enums.Color;
import com.oz.product.enums.TypeOfThing;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CacheConcurrencyStrategy;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "typeOfThing")
@Entity
@Cacheable // Включаем кэш для этой сущности
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "products", indexes = {
        @Index(name = "idx_products_name", columnList = "name"),
        @Index(name = "idx_products_price", columnList = "price"),
        @Index(name = "idx_products_color", columnList = "color"),
        @Index(name = "idx_products_typeOfThing", columnList = "typeOfThing")
})
public class Product extends com.oz.common.entity.Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(value= EnumType.STRING)
    @Column(name = "typeOfThing", insertable = false, updatable = false)
    private TypeOfThing typeOfThing;

    public Product(String name,
                   Long count,
                   BigDecimal price,
                   LocalDate dateOfEntry,
                   Color c,
                   UUID id,
                   TypeOfThing typeOfThing) {
        super(name, count, price, dateOfEntry, c);
        this.id = id;
        this.typeOfThing = typeOfThing;
    }
    public Product(){}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TypeOfThing getTypeOfThing() {
        return typeOfThing;
    }

    public void setTypeOfThing(TypeOfThing typeOfThing) {
        this.typeOfThing = typeOfThing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
