package com.pau.flight_sync.adapters.in.web.image;

import com.pau.flight_sync.application.usecase.image.DeleteFlightImageUseCase;
import com.pau.flight_sync.application.usecase.image.UploadFlightImageUseCase;
import com.pau.flight_sync.domain.port.FlightImageStoragePort;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class FlightImageController {

    private final UploadFlightImageUseCase uploadFlightImage;
    private final DeleteFlightImageUseCase deleteFlightImage;
    private final FlightImageStoragePort   imageStorage;

    @PostMapping("/flights/{id}/images")
    public String uploadImage(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Selecciona una imatge.");
            return "redirect:/flights/" + id;
        }
        try {
            uploadFlightImage.execute(id, file);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error en desar la imatge.");
        }
        return "redirect:/flights/" + id;
    }

    @PostMapping("/flights/{id}/images/delete")
    public String deleteImage(
            @PathVariable UUID id,
            @RequestParam("filename") String filename,
            RedirectAttributes redirectAttributes) {
        try {
            deleteFlightImage.execute(id, filename);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error en eliminar la imatge.");
        }
        return "redirect:/flights/" + id;
    }

    @GetMapping("/flight-images/{flightId}/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> serveImage(
            @PathVariable UUID flightId,
            @PathVariable String filename) {
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            return ResponseEntity.badRequest().build();
        }
        val imagePath = imageStorage.resolveImagePath(flightId, filename);
        val resource = new FileSystemResource(imagePath);
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }
        val contentType = filename.toLowerCase().endsWith(".png") ? "image/png" : "image/jpeg";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CACHE_CONTROL, "max-age=86400")
                .body(resource);
    }
}
