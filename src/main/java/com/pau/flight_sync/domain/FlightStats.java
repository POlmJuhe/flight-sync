package com.pau.flight_sync.domain;

public record FlightStats(
        long totalFlights,
        long totalFlightTimeMinutes,
        long totalDistanceKm,
        String mostUsedAircraft
) {}
