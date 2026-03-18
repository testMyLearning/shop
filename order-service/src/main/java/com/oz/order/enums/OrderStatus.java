package com.oz.order.enums;

public enum OrderStatus {
    PENDING("Создан"),
    PAYMENT_PROCESSING("Обработка платежа"),
    INVENTORY_RESERVED("Зарезервировано"),
    COMPLETED("Завершен"),
    CANCELLED("Удалён");

    private final String description;

    OrderStatus(String desc) {
        this.description = desc;
    }

    public String getDescription() {
        return description;

    }
}
