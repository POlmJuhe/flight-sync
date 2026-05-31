package com.pau.flight_sync.application.usecase.flight;

import com.pau.flight_sync.application.dto.DashboardStatsDto;
import com.pau.flight_sync.application.dto.DashboardView;
import com.pau.flight_sync.application.dto.FlightDto;
import com.pau.flight_sync.domain.model.Flight;
import com.pau.flight_sync.domain.port.AirportLookupPort;
import com.pau.flight_sync.domain.repository.FlightRepositoryPort;
import com.pau.flight_sync.domain.valueobject.FlightDuration;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetDashboardUseCase {

    private final FlightRepositoryPort flightRepository;
    private final AirportLookupPort    airportLookup;

    @Transactional(readOnly = true)
    public DashboardView execute(int page,
                                 String registration, String flightType,
                                 Integer year, String airport) {
        val pageable   = PageRequest.of(page, 20, Sort.by("departureTime").descending());
        val anyFilter  = registration != null || flightType != null || year != null || airport != null;

        val flights = anyFilter
                ? flightRepository.findFiltered(registration, flightType, year, airport, pageable).map(this::toDto)
                : flightRepository.findAll(pageable).map(this::toDto);

        val rawStats = anyFilter
                ? flightRepository.getFilteredStats(registration, flightType, year, airport)
                : flightRepository.getStats();

        val stats = new DashboardStatsDto(
                rawStats.totalFlights(),
                FlightDuration.ofMinutes(rawStats.totalFlightTimeMinutes()).toDisplayString(),
                rawStats.totalDistanceKm(),
                rawStats.mostUsedAircraft());

        return new DashboardView(
                stats,
                flights,
                flightRepository.findDistinctRegistrations(),
                flightRepository.findDistinctYears());
    }

    private FlightDto toDto(Flight flight) {
        return new FlightDto(
                flight.getId(),
                flight.getDepartureTime().toLocalDate(),
                flight.getRegistration(),
                buildRoute(flight),
                flight.getDistanceKm(),
                flight.getDuration().toDisplayString(),
                mapCrew(flight));
    }

    private List<String> mapCrew(Flight flight) {
        if (flight.getCrew() == null) return List.of();
        return flight.getCrew().stream()
                .map(m -> m.getFullName() + " (" + m.getFunction() + ")")
                .toList();
    }

    private String buildRoute(Flight flight) {
        val dep = resolve(flight.getDepartureAirport().code());
        val arr = resolve(flight.getArrivalAirport().code());
        return dep + " → " + arr;
    }

    private String resolve(String icaoCode) {
        return airportLookup.findName(icaoCode)
                .map(name -> icaoCode + " (" + name + ")")
                .orElse(icaoCode);
    }
}
