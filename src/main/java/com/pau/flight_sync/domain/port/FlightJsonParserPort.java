package com.pau.flight_sync.domain.port;

import com.pau.flight_sync.domain.model.Flight;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface FlightJsonParserPort {
    List<Flight> parse(InputStream stream) throws IOException;
}
