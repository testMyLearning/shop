package com.oz.product.entity;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("JACKET")
public class JacketProduct extends Product{
    private String jacketMaterial;
}
