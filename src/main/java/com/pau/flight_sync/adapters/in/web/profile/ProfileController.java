package com.pau.flight_sync.adapters.in.web.profile;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pau.flight_sync.adapters.out.imagestore.ProfileImageAdapter;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileImageAdapter profileImageAdapter;

    @GetMapping("/profile-image")
    @ResponseBody
    public ResponseEntity<Resource> serveProfileImage() {
        return profileImageAdapter.getPath()
                .map(path -> {
                    val resource = (Resource) new FileSystemResource(path);
                    val mediaType = path.toString().endsWith(".png") ? "image/png" : "image/jpeg";
                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(mediaType))
                            .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store")
                            .body(resource);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/profile-image")
    public String uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if (!file.isEmpty()) {
            try {
                profileImageAdapter.save(file);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error en desar la foto de perfil.");
            }
        }
        return "redirect:/";
    }
}
