package com.pau.flight_sync.application.usecase.flight;

import com.pau.flight_sync.domain.port.AirportLookupPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GetAirportsUseCase {

    private final AirportLookupPort airportLookup;

    public Map<String, String> execute() {
        return airportLookup.findAll();
    }
}
