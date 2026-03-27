package com.oz.product.entity;

import com.oz.product.enums.TypeOfThing;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Tshirt")
public class Tshirt extends Product{
    private int TShirtsSize=99;
    public Tshirt(){
        this.setTypeOfThing(TypeOfThing.Tshirt);
    }
}
