package com.pau.flight_sync.application.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record FlightDto(
        UUID         id,
        LocalDate    date,
        String       registration,
        String       route,
        int          distanceKm,
        String       duration,
        List<String> crew
) {}
