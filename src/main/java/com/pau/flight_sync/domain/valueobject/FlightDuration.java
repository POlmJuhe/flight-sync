package com.pau.flight_sync.domain.valueobject;

import java.time.Duration;
import java.time.LocalDateTime;

public record FlightDuration(long minutes) {

    public static FlightDuration between(LocalDateTime start, LocalDateTime end) {
        return new FlightDuration(Duration.between(start, end).toMinutes());
    }

    public static FlightDuration ofMinutes(long minutes) {
        return new FlightDuration(minutes);
    }

    public String toDisplayString() {
        long hours = minutes / 60;
        long mins  = minutes % 60;
        return String.format("%dh %02dm", hours, mins);
    }
}
