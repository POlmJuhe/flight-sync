package com.pau.flight_sync.application.usecase.image;

import com.pau.flight_sync.domain.port.FlightImageStoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetFlightImagesUseCase {

    private final FlightImageStoragePort imageStorage;

    public List<String> execute(UUID flightId) {
        return imageStorage.listImages(flightId);
    }
}
