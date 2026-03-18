package com.oz.product.enums;

public enum TypeOfThing {
    Jacket("Куртка"),
    Cap("Кепка"),
    Tshirt("Футболка"),
    Trousers("Штаны");

    private final String name;

    TypeOfThing(String s) {
        this.name = s;

    }
    public String getName(){
        return  name;
    }

    public static TypeOfThing fromName(String name){
        for(TypeOfThing type : values()){
            if(name.equalsIgnoreCase(type.getName())){
                return type;
            }
        }
        throw new IllegalArgumentException("Ошибка перевода названия вещи в тип");
    }
}
