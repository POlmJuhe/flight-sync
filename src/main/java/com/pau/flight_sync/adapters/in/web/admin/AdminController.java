package com.pau.flight_sync.adapters.in.web.admin;

import com.pau.flight_sync.application.dto.ImportResultDto;
import com.pau.flight_sync.application.usecase.flight.ImportFlightsUseCase;
import com.pau.flight_sync.application.usecase.flight.ImportFlightsUseCase.FlightImportException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final ImportFlightsUseCase importFlights;

    @GetMapping("/admin")
    public String adminPage() {
        return "import";
    }

    @PostMapping("/admin/import")
    public String importFlights(
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Selecciona un fitxer JSON.");
            return "redirect:/admin";
        }
        try {
            val result = importFlights.execute(file.getInputStream());
            redirectAttributes.addFlashAttribute("successMessage",
                    "Importats " + result.imported() + " vols. Ignorats " + result.skipped() + " duplicats.");
        } catch (FlightImportException | IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error en importar: " + e.getMessage());
        }
        return "redirect:/admin";
    }
}
