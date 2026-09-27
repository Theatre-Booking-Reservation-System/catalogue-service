package com.theatre.catalogueservice.util;

public enum Availability {
    // Less than 75% of seats booked.
    AVAILABLE,
    // 75% or more booked, but not all.
    LIMITED_SEATS,
    // Every seat booked.
    FULLY_BOOKED
}
