package com.theatre.catalogueservice.util;

public enum PerformanceStatus {

    SCHEDULED(1),
    CANCELLED(9);

    private final int value;

    PerformanceStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PerformanceStatus fromValue(int value) {
        for (PerformanceStatus status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown PerformanceStatus value: " + value);
    }
}
