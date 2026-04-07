package com.oz.common.entity;

import com.oz.common.enums.Color;
import jakarta.persistence.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Product {
    protected String name;
    protected Long count;
    protected BigDecimal price;
    @Column(name = "date_of_entry", updatable = false)
    @CreatedDate
    protected LocalDate dateOfEntry;
    @Enumerated(EnumType.STRING)
    protected Color color;



    @LastModifiedDate
    protected LocalDateTime updatedAt;

    protected Product(){}
    protected Product(String name, Long count, BigDecimal price, LocalDate dateOfEntry,Color c) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.dateOfEntry = dateOfEntry;
        this.color=c;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDate getDateOfEntry() {
        return dateOfEntry;
    }

    public void setDateOfEntry(LocalDate dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
