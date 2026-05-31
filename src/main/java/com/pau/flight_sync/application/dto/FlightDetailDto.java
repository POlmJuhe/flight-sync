package com.pau.flight_sync.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record FlightDetailDto(
        UUID            id,
        String          registration,
        String          route,
        String          departureAirportCode,
        String          arrivalAirportCode,
        String          flightType,
        String          flightTypeKey,
        LocalDateTime   blockStart,
        LocalDateTime   departureTime,
        LocalDateTime   landingTime,
        LocalDateTime   blockEnd,
        String          duration,
        String          blockTime,
        int             distanceKm,
        int             nbLandings,
        boolean         night,
        boolean         crossCountry,
        String          remarks,
        List<CrewMemberDto> crew
) {
    public record CrewMemberDto(UUID id, String fullName, String firstName, String lastName, String function) {}

    // SpEL (Thymeleaf) resolves boolean props via isX() — records only generate x(), so we add both
    public boolean isNight()        { return night; }
    public boolean isCrossCountry() { return crossCountry; }
}
