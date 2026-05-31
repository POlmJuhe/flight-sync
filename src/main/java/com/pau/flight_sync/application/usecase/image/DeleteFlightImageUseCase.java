package com.pau.flight_sync.application.usecase.image;

import com.pau.flight_sync.domain.port.FlightImageStoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteFlightImageUseCase {

    private final FlightImageStoragePort imageStorage;

    public void execute(UUID flightId, String filename) {
        imageStorage.delete(flightId, filename);
    }
}
