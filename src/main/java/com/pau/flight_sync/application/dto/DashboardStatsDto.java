package com.pau.flight_sync.application.dto;

public record DashboardStatsDto(
        long   totalFlights,
        String totalFlightTime,
        long   totalDistanceKm,
        String mostUsedAircraft
) {}
