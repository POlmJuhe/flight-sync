package com.pau.flight_sync.application.usecase.flight;

import com.pau.flight_sync.application.dto.DashboardStatsDto;
import com.pau.flight_sync.domain.repository.FlightRepositoryPort;
import com.pau.flight_sync.domain.valueobject.FlightDuration;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetDashboardStatsUseCase {

    private final FlightRepositoryPort flightRepository;

    @Transactional(readOnly = true)
    public DashboardStatsDto execute() {
        val stats = flightRepository.getStats();
        val totalFlightTime = FlightDuration.ofMinutes(stats.totalFlightTimeMinutes()).toDisplayString();
        return new DashboardStatsDto(
                stats.totalFlights(),
                totalFlightTime,
                stats.totalDistanceKm(),
                stats.mostUsedAircraft()
        );
    }
}
