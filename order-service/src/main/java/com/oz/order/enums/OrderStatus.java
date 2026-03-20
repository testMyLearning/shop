package com.oz.order.enums;

public enum OrderStatus {
    PENDING("Создан"),
    PAYMENT_PROCESSING("Обработка платежа"),
    INVENTORY_RESERVED("Зарезервировано"),
    COMPLETED("Завершен"),
    CANCELLED("Удалён"),
    ERROR_OUT_OF_STOCKS("Нет на складе");

    private final String description;

    OrderStatus(String desc) {
        this.description = desc;
    }

    public String getDescription() {
        return description;

    }
}
