package com.pau.flight_sync.adapters.out.imagestore;

import com.pau.flight_sync.domain.port.FlightImageStoragePort;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Component
public class FilesystemFlightImageAdapter implements FlightImageStoragePort {

    private final Path basePath;

    public FilesystemFlightImageAdapter(@Value("${app.images.base-path}") String basePath) {
        this.basePath = Paths.get(basePath);
    }

    @Override
    public List<String> listImages(UUID flightId) {
        Path dir = flightDir(flightId);
        if (!Files.exists(dir)) return Collections.emptyList();
        try (Stream<Path> files = Files.list(dir)) {
            return files
                    .map(p -> p.getFileName().toString())
                    .filter(name -> name.startsWith("orig_"))
                    .map(name -> name.substring("orig_".length()))
                    .sorted()
                    .toList();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void save(UUID flightId, String filename, InputStream data) throws IOException {
        Path dir = flightDir(flightId);
        Files.createDirectories(dir);

        Path origPath = dir.resolve("orig_" + filename);
        Files.copy(data, origPath);

        Path thumbPath = dir.resolve("thumb_" + filename);
        Thumbnails.of(origPath.toFile())
                .width(400)
                .keepAspectRatio(true)
                .toFile(thumbPath.toFile());
    }

    @Override
    public void delete(UUID flightId, String filename) {
        Path dir = flightDir(flightId);
        try {
            Files.deleteIfExists(dir.resolve("orig_" + filename));
            Files.deleteIfExists(dir.resolve("thumb_" + filename));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public Path resolveImagePath(UUID flightId, String filename) {
        return flightDir(flightId).resolve(filename);
    }

    private Path flightDir(UUID flightId) {
        return basePath.resolve(flightId.toString());
    }
}
