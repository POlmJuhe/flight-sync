package com.pau.flight_sync.application.usecase.flight;

import com.pau.flight_sync.application.dto.ImportResultDto;
import com.pau.flight_sync.domain.model.Flight;
import com.pau.flight_sync.domain.port.FlightJsonParserPort;
import com.pau.flight_sync.domain.repository.FlightRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportFlightsUseCase {

    private final FlightRepositoryPort flightRepository;
    private final FlightJsonParserPort jsonParser;

    @Transactional
    public ImportResultDto execute(InputStream jsonStream) {
        val flights = parse(jsonStream);

        var imported = 0;
        var skipped  = 0;
        for (val flight : flights) {
            if (flightRepository.existsByExternalId(flight.getExternalId())) {
                skipped++;
                continue;
            }
            flightRepository.save(flight);
            imported++;
        }
        return new ImportResultDto(imported, skipped);
    }

    private List<Flight> parse(InputStream stream) {
        try {
            return jsonParser.parse(stream);
        } catch (IOException e) {
            throw new FlightImportException("Error parsing JSON: " + e.getMessage(), e);
        }
    }

    public static class FlightImportException extends RuntimeException {
        public FlightImportException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
