package com.pau.flight_sync.application.usecase.flight;

import com.pau.flight_sync.application.dto.CarnetDto;
import com.pau.flight_sync.domain.repository.FlightRepositoryPort;
import com.pau.flight_sync.domain.valueobject.FlightDuration;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class GetCarnetStatsUseCase {

    private final FlightRepositoryPort flightRepository;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Transactional(readOnly = true)
    public CarnetDto execute() {
        val stats         = flightRepository.getStats();
        val firstDate     = flightRepository.findFirstFlightDate();
        val soloFlights   = flightRepository.countByFlightType("SOLO");
        val instrFlights  = flightRepository.countByFlightType("INSTR");
        val topAirport    = flightRepository.findTopAirport();

        return new CarnetDto(
                stats.totalFlights(),
                FlightDuration.ofMinutes(stats.totalFlightTimeMinutes()).toDisplayString(),
                stats.totalDistanceKm(),
                stats.mostUsedAircraft(),
                topAirport != null ? topAirport : "—",
                firstDate  != null ? firstDate.format(FMT) : "—",
                soloFlights,
                instrFlights
        );
    }
}
