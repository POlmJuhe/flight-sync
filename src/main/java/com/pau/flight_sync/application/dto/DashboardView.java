package com.pau.flight_sync.application.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record DashboardView(
        DashboardStatsDto  stats,
        Page<FlightDto>    flights,
        List<String>       registrations,
        List<Integer>      years
) {}
