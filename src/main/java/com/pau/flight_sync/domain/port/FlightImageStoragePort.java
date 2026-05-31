package com.pau.flight_sync.domain.port;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public interface FlightImageStoragePort {
    List<String> listImages(UUID flightId);
    void save(UUID flightId, String filename, InputStream data) throws IOException;
    void delete(UUID flightId, String filename);
    Path resolveImagePath(UUID flightId, String filename);
}
