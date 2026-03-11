package com.oz.product.entity;

import com.oz.common.enums.Color;
import com.oz.product.enums.TypeOfThing;
import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
@Entity
@Table(name = "products")
public class Product extends com.oz.common.entity.Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(value= EnumType.STRING)
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
