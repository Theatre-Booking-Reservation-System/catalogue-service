package com.theatre.catalogueservice.util;

public enum ProductionStatus {

    ACTIVE(1),
    ARCHIVED(9);

    private final int value;

    ProductionStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ProductionStatus fromValue(int value) {
        for (ProductionStatus status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown ProductionStatus value: " + value);
    }
}
