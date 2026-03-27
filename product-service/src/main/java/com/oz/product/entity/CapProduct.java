package com.oz.product.entity;

import com.oz.product.enums.TypeOfThing;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Cap")
public class CapProduct extends Product{

    private int capSize=10;

    public CapProduct(){
        this.setTypeOfThing(TypeOfThing.Cap);
    }

}
