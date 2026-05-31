package com.pau.flight_sync.application.dto;

public record CarnetDto(
        long   totalFlights,
        String totalFlightTime,
        long   totalDistanceKm,
        String mostUsedAircraft,
        String topAirport,
        String pilotSince,
        long   soloFlights,
        long   instrFlights
) {}
