package com.oz.product.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.oz.common.exception.CustomException;

public enum TypeOfThing {
    Jacket("Куртка"),
    Cap("Кепка"),
    Tshirt("Футболка"),
    Trousers("Штаны");

    private final String name;

    TypeOfThing(String s) {
        this.name = s;

    }
    @JsonValue
    public String getName(){
        return  name;
    }
@JsonCreator
    public static TypeOfThing fromName(String name){
        String trimmedValue=name.trim();
        for(TypeOfThing type : values()){
            if(trimmedValue.equalsIgnoreCase(type.getName())){
                return type;
            }
        }
        throw new CustomException("Ошибка перевода названия вещи в тип");
    }
}
