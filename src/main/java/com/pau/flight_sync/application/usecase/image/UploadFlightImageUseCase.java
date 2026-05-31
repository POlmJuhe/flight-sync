package com.pau.flight_sync.application.usecase.image;

import com.pau.flight_sync.domain.port.FlightImageStoragePort;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadFlightImageUseCase {

    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png");

    private final FlightImageStoragePort imageStorage;

    public void execute(UUID flightId, MultipartFile file) throws IOException {
        val contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Només s'accepten imatges JPEG o PNG.");
        }
        val ext = "image/png".equals(contentType) ? ".png" : ".jpg";
        val filename = UUID.randomUUID() + ext;
        imageStorage.save(flightId, filename, file.getInputStream());
    }
}
