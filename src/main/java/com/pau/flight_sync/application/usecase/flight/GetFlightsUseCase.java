package com.pau.flight_sync.application.usecase.flight;

import com.pau.flight_sync.application.dto.FlightDto;
import com.pau.flight_sync.domain.model.Flight;
import com.pau.flight_sync.domain.port.AirportLookupPort;
import com.pau.flight_sync.domain.repository.FlightRepositoryPort;
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
public class GetFlightsUseCase {

    private final FlightRepositoryPort flightRepository;
    private final AirportLookupPort    airportLookup;

    @Transactional(readOnly = true)
    public Page<FlightDto> execute(int page, int size,
                                   String registration, String flightType,
                                   Integer year, String airport) {
        val pageable = PageRequest.of(page, size, Sort.by("departureTime").descending());
        val anyFilter = registration != null || flightType != null || year != null || airport != null;
        return anyFilter
                ? flightRepository.findFiltered(registration, flightType, year, airport, pageable).map(this::toDto)
                : flightRepository.findAll(pageable).map(this::toDto);
    }

    public List<String>  getDistinctRegistrations() { return flightRepository.findDistinctRegistrations(); }
    public List<Integer> getDistinctYears()          { return flightRepository.findDistinctYears(); }

    private FlightDto toDto(Flight flight) {
        return new FlightDto(
                flight.getId(),
                flight.getDepartureTime().toLocalDate(),
                flight.getRegistration(),
                buildRoute(flight),
                flight.getDistanceKm(),
                flight.getDuration().toDisplayString(),
                mapCrew(flight)
        );
    }

    private List<String> mapCrew(Flight flight) {
        if (flight.getCrew() == null) return List.of();
        return flight.getCrew().stream()
                .map(member -> member.getFullName() + " (" + member.getFunction() + ")")
                .toList();
    }

    private String buildRoute(Flight flight) {
        val departure = resolve(flight.getDepartureAirport().code());
        val arrival   = resolve(flight.getArrivalAirport().code());
        return departure + " → " + arrival;
    }

    private String resolve(String icaoCode) {
        return airportLookup.findName(icaoCode)
                .map(name -> icaoCode + " (" + name + ")")
                .orElse(icaoCode);
    }
}
