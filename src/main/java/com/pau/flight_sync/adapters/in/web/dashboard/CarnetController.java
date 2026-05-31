package com.pau.flight_sync.adapters.in.web.dashboard;

import com.pau.flight_sync.application.dto.CarnetDto;
import com.pau.flight_sync.application.usecase.flight.GetCarnetStatsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CarnetController {

    private final GetCarnetStatsUseCase getCarnetStats;

    @GetMapping("/carnet")
    public ResponseEntity<CarnetDto> carnet() {
        return ResponseEntity.ok(getCarnetStats.execute());
    }
}
