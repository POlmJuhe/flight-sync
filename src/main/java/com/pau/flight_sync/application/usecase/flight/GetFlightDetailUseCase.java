package com.pau.flight_sync.application.usecase.flight;

import com.pau.flight_sync.application.dto.FlightDetailDto;
import com.pau.flight_sync.application.dto.FlightDetailDto.CrewMemberDto;
import com.pau.flight_sync.domain.model.Flight;
import com.pau.flight_sync.domain.port.AirportLookupPort;
import com.pau.flight_sync.domain.repository.FlightRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetFlightDetailUseCase {

    private final FlightRepositoryPort flightRepository;
    private final AirportLookupPort    airportLookup;

    @Transactional(readOnly = true)
    public Optional<FlightDetailDto> execute(UUID id) {
        return flightRepository.findById(id).map(this::toDto);
    }

    private FlightDetailDto toDto(Flight flight) {
        val departure = resolve(flight.getDepartureAirport().code());
        val arrival   = resolve(flight.getArrivalAirport().code());
        val route     = departure + " → " + arrival;

        return new FlightDetailDto(
                flight.getId(),
                flight.getRegistration(),
                route,
                flight.getDepartureAirport().code(),
                flight.getArrivalAirport().code(),
                flight.getFlightType().getLabel(),
                flight.getFlightType().name(),
                flight.getBlockStart(),
                flight.getDepartureTime(),
                flight.getLandingTime(),
                flight.getBlockEnd(),
                flight.getDuration().toDisplayString(),
                flight.getBlockTime().toDisplayString(),
                flight.getDistanceKm(),
                flight.getNbLandings() != null ? flight.getNbLandings() : 0,
                Boolean.TRUE.equals(flight.getNight()),
                Boolean.TRUE.equals(flight.getCrossCountry()),
                flight.getRemarks(),
                mapCrew(flight)
        );
    }

    private List<CrewMemberDto> mapCrew(Flight flight) {
        if (flight.getCrew() == null) return List.of();
        return flight.getCrew().stream()
                .map(member -> new CrewMemberDto(member.getId(), member.getFullName(),
                        member.getFirstName(), member.getLastName(), member.getFunction()))
                .toList();
    }

    private String resolve(String icaoCode) {
        return airportLookup.findName(icaoCode)
                .map(name -> icaoCode + " (" + name + ")")
                .orElse(icaoCode);
    }
}
