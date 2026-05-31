package com.pau.flight_sync.application.usecase.flight;

import com.pau.flight_sync.application.dto.CreateFlightForm;
import com.pau.flight_sync.domain.FlightType;
import com.pau.flight_sync.domain.model.Flight;
import com.pau.flight_sync.domain.repository.FlightRepositoryPort;
import com.pau.flight_sync.domain.valueobject.AirportCode;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateFlightUseCase {

    private final FlightRepositoryPort flightRepository;

    @Transactional
    public UUID execute(CreateFlightForm form) {
        val flightId = UUID.randomUUID();
        val flight = Flight.builder()
                .id(flightId)
                .externalId(null)
                .registration(form.getRegistration().trim().toUpperCase())
                .departureAirport(new AirportCode(form.getDepartureAirport()))
                .arrivalAirport(new AirportCode(form.getArrivalAirport()))
                .blockStart(form.getBlockStart())
                .departureTime(form.getDepartureTime())
                .landingTime(form.getLandingTime())
                .blockEnd(form.getBlockEnd())
                .distanceKm(form.getDistanceKm() != null ? form.getDistanceKm() : 0)
                .flightType(FlightType.fromString(form.getFlightType()))
                .nbLandings(form.getNbLandings() != null ? form.getNbLandings() : 0)
                .night(form.isNight())
                .crossCountry(form.isCrossCountry())
                .remarks(form.getRemarks() != null ? form.getRemarks().strip() : null)
                .crew(List.of())
                .build();

        flightRepository.save(flight);
        return flightId;
    }
}
