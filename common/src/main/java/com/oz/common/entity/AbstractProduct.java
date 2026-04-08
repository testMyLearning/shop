package com.oz.common.entity;

import com.oz.common.enums.Color;
import jakarta.persistence.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Setter
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Audited
@EqualsAndHashCode
public abstract class AbstractProduct {
    protected String name;
    protected Long count;
    protected BigDecimal price;
    @Column(name = "date_of_entry", updatable = false)
    @CreationTimestamp
    protected LocalDate dateOfEntry;
    @Enumerated(EnumType.STRING)
    protected Color color;



    @UpdateTimestamp
    protected LocalDateTime updatedAt;

    protected AbstractProduct(){}
    protected AbstractProduct(String name, Long count, BigDecimal price, LocalDate dateOfEntry, Color c) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.dateOfEntry = dateOfEntry;
        this.color=c;
    }

}
