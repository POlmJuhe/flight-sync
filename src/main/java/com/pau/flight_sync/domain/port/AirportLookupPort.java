package com.pau.flight_sync.domain.port;

import java.util.Map;
import java.util.Optional;

public interface AirportLookupPort {
    Optional<String> findName(String icaoCode);
    Map<String, String> findAll();
}
