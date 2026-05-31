package com.pau.flight_sync.adapters.in.web.flight;

import com.pau.flight_sync.application.dto.AddCrewMemberForm;
import com.pau.flight_sync.application.dto.CreateFlightForm;
import com.pau.flight_sync.application.usecase.flight.CreateFlightUseCase;
import com.pau.flight_sync.application.usecase.flight.DeleteFlightUseCase;
import com.pau.flight_sync.application.usecase.flight.GetAirportsUseCase;
import com.pau.flight_sync.application.usecase.flight.GetFlightDetailUseCase;
import com.pau.flight_sync.application.usecase.image.GetFlightImagesUseCase;
import com.pau.flight_sync.application.usecase.flight.UpdateFlightUseCase;
import com.pau.flight_sync.domain.FlightType;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class FlightController {

    private final GetFlightDetailUseCase getFlightDetail;
    private final GetFlightImagesUseCase getFlightImages;
    private final CreateFlightUseCase    createFlight;
    private final DeleteFlightUseCase    deleteFlight;
    private final UpdateFlightUseCase    updateFlight;
    private final GetAirportsUseCase     getAirports;

    @GetMapping("/flights/new")
    public String newFlightPage(Model model) {
        model.addAttribute("form",        new CreateFlightForm());
        model.addAttribute("flightTypes", List.of(FlightType.INSTR, FlightType.SOLO));
        model.addAttribute("airports",    getAirports.execute());
        model.addAttribute("editMode",    false);
        return "flight-form";
    }

    @GetMapping("/flights/{id}/edit")
    public String editFlightPage(@PathVariable UUID id, Model model) {
        return getFlightDetail.execute(id)
                .map(detail -> {
                    val form = new CreateFlightForm();
                    form.setRegistration(detail.registration());
                    form.setDepartureAirport(detail.departureAirportCode());
                    form.setArrivalAirport(detail.arrivalAirportCode());
                    form.setBlockStart(detail.blockStart());
                    form.setDepartureTime(detail.departureTime());
                    form.setLandingTime(detail.landingTime());
                    form.setBlockEnd(detail.blockEnd());
                    form.setDistanceKm(detail.distanceKm());
                    form.setFlightType(detail.flightTypeKey());
                    form.setNbLandings(detail.nbLandings());
                    form.setNight(detail.night());
                    form.setCrossCountry(detail.crossCountry());
                    form.setRemarks(detail.remarks());
                    model.addAttribute("form",        form);
                    model.addAttribute("flightTypes", List.of(FlightType.INSTR, FlightType.SOLO));
                    model.addAttribute("airports",    getAirports.execute());
                    model.addAttribute("editMode",    true);
                    model.addAttribute("flightId",    id);
                    return "flight-form";
                })
                .orElse("redirect:/");
    }

    @GetMapping("/flights/{id}")
    public String flightDetail(@PathVariable UUID id, Model model) {
        return getFlightDetail.execute(id)
                .map(detail -> {
                    model.addAttribute("flight",      detail);
                    model.addAttribute("crewForm",    new AddCrewMemberForm());
                    model.addAttribute("editForm",    new CreateFlightForm());
                    model.addAttribute("flightTypes", List.of(FlightType.INSTR, FlightType.SOLO));
                    model.addAttribute("airports",    getAirports.execute());
                    model.addAttribute("images",      getFlightImages.execute(id));
                    return "flight-detail";
                })
                .orElse("redirect:/");
    }

    @PostMapping("/admin/flights")
    public String createFlight(
            @ModelAttribute("form") CreateFlightForm form,
            RedirectAttributes redirectAttributes) {
        if (isBlank(form.getRegistration()) || isBlank(form.getDepartureAirport())
                || isBlank(form.getArrivalAirport())
                || form.getDepartureTime() == null || form.getLandingTime() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Matrícula, aeroports i horaris de vol són obligatoris.");
            return "redirect:/";
        }
        try {
            UUID newId = createFlight.execute(form);
            redirectAttributes.addFlashAttribute("successMessage", "Vol creat correctament.");
            return "redirect:/flights/" + newId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error en crear el vol: " + e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/flights/{id}/delete")
    public String deleteFlight(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            deleteFlight.execute(id);
            redirectAttributes.addFlashAttribute("successMessage", "Vol eliminat.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error en eliminar el vol: " + e.getMessage());
            return "redirect:/flights/" + id;
        }
        return "redirect:/";
    }

    @PostMapping("/flights/{id}/edit")
    public String editFlight(
            @PathVariable UUID id,
            @ModelAttribute("editForm") CreateFlightForm form,
            RedirectAttributes redirectAttributes) {
        if (isBlank(form.getRegistration()) || isBlank(form.getDepartureAirport())
                || isBlank(form.getArrivalAirport())
                || form.getDepartureTime() == null || form.getLandingTime() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Matrícula, aeroports i horaris són obligatoris.");
            return "redirect:/flights/" + id;
        }
        try {
            updateFlight.execute(id, form);
            redirectAttributes.addFlashAttribute("successMessage", "Vol actualitzat.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error en actualitzar el vol: " + e.getMessage());
        }
        return "redirect:/flights/" + id;
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
