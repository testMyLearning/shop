package com.oz.common.entity;

import com.oz.common.enums.Color;
import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Product {
    protected String name;
    protected Long count;
    protected BigDecimal price;
    protected LocalDate dateOfEntry;
    protected Color color;

    protected Product(){}
    public Product(String name, Long count, BigDecimal price, LocalDate dateOfEntry,Color c) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.dateOfEntry = LocalDate.now();
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

    public LocalDate getDateOfEntry() {
        return dateOfEntry;
    }

    public void setDateOfEntry(LocalDate dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }
}
