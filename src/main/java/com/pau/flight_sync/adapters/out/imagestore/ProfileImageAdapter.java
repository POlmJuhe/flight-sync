package com.pau.flight_sync.adapters.out.imagestore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Component
public class ProfileImageAdapter {

    private final Path dir;

    public ProfileImageAdapter(@Value("${app.images.base-path}") String basePath) {
        this.dir = Paths.get(basePath, "profile");
    }

    public void save(MultipartFile file) throws IOException {
        Files.createDirectories(dir);
        Files.deleteIfExists(dir.resolve("photo.jpg"));
        Files.deleteIfExists(dir.resolve("photo.png"));
        String ext = "image/png".equals(file.getContentType()) ? "png" : "jpg";
        Files.copy(file.getInputStream(), dir.resolve("photo." + ext));
    }

    public Optional<Path> getPath() {
        for (String ext : java.util.List.of("jpg", "png")) {
            Path p = dir.resolve("photo." + ext);
            if (Files.exists(p)) return Optional.of(p);
        }
        return Optional.empty();
    }
}
