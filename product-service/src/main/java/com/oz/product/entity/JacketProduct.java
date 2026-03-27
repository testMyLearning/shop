package com.oz.product.entity;


import com.oz.product.enums.TypeOfThing;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Jacket")
public class JacketProduct extends Product{
    private String jacketMaterial = "кожа";
    public JacketProduct(){
        this.setTypeOfThing(TypeOfThing.Jacket);
    }
}
