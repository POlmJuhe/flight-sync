package com.pau.flight_sync.application.usecase.flight;

import com.pau.flight_sync.application.dto.CreateFlightForm;
import com.pau.flight_sync.domain.FlightType;
import com.pau.flight_sync.domain.repository.FlightRepositoryPort;
import com.pau.flight_sync.domain.valueobject.AirportCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateFlightUseCase {

    private final FlightRepositoryPort flightRepository;

    @Transactional
    public void execute(UUID id, CreateFlightForm form) {
        flightRepository.updateFlight(
                id,
                form.getRegistration().trim().toUpperCase(),
                FlightType.fromString(form.getFlightType()).name(),
                new AirportCode(form.getDepartureAirport()).code(),
                new AirportCode(form.getArrivalAirport()).code(),
                form.getBlockStart(),
                form.getDepartureTime(),
                form.getLandingTime(),
                form.getBlockEnd(),
                form.getDistanceKm(),
                form.getNbLandings(),
                form.isNight(),
                form.isCrossCountry(),
                form.getRemarks()
        );
    }
}
