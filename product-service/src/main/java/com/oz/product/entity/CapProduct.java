package com.oz.product.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CAP")
public class CapProduct extends Product{
    private int capSize;

}
