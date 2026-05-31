package com.pau.flight_sync.domain.valueobject;

import java.util.Objects;

public record AirportCode(String code) {

    public AirportCode {
        Objects.requireNonNull(code, "Airport code must not be null");
        String trimmed = code.trim().toUpperCase();
        if (trimmed.isBlank() || trimmed.length() > 4) {
            throw new IllegalArgumentException("Invalid ICAO code: " + code);
        }
        code = trimmed;
    }

    @Override
    public String toString() {
        return code;
    }
}
