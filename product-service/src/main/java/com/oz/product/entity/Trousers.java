package com.oz.product.entity;

import com.oz.product.enums.TypeOfThing;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Trousers")
public class Trousers extends Product{
    private int trousersLength=100;
    public Trousers(){
        this.setTypeOfThing(TypeOfThing.Trousers);
    }
}
