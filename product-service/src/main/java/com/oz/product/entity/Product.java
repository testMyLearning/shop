package com.oz.product.entity;

import com.oz.common.entity.AbstractProduct;
import com.oz.common.enums.Color;
import com.oz.product.enums.TypeOfThing;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "typeOfThing")
@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_products_name", columnList = "name"),
        @Index(name = "idx_products_price", columnList = "price"),
        @Index(name = "idx_products_color", columnList = "color"),
        @Index(name = "idx_products_typeOfThing", columnList = "typeOfThing")
})
//@Audited
//@AuditOverride(forClass = AbstractProduct.class)
@Getter
@Setter
@NoArgsConstructor
public class Product extends AbstractProduct {
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
