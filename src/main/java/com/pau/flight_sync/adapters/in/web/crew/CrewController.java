package com.pau.flight_sync.adapters.in.web.crew;

import com.pau.flight_sync.application.dto.AddCrewMemberForm;
import com.pau.flight_sync.application.usecase.crew.AddCrewMemberUseCase;
import com.pau.flight_sync.application.usecase.crew.RemoveCrewMemberUseCase;
import com.pau.flight_sync.application.usecase.crew.UpdateCrewMemberUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class CrewController {

    private final AddCrewMemberUseCase    addCrewMember;
    private final RemoveCrewMemberUseCase removeCrewMember;
    private final UpdateCrewMemberUseCase updateCrewMember;

    @PostMapping("/flights/{id}/crew")
    public String addCrew(
            @PathVariable UUID id,
            @ModelAttribute AddCrewMemberForm form,
            RedirectAttributes redirectAttributes) {
        if (isBlank(form.getFirstName()) || isBlank(form.getLastName()) || isBlank(form.getFunction())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nom, cognom i funció són obligatoris.");
            return "redirect:/flights/" + id;
        }
        try {
            addCrewMember.execute(id, form);
            redirectAttributes.addFlashAttribute("successMessage", "Membre de tripulació afegit.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error en afegir tripulació: " + e.getMessage());
        }
        return "redirect:/flights/" + id;
    }

    @PostMapping("/flights/{id}/crew/{crewId}/delete")
    public String deleteCrewMember(
            @PathVariable UUID id,
            @PathVariable UUID crewId,
            RedirectAttributes redirectAttributes) {
        try {
            removeCrewMember.execute(id, crewId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error en eliminar tripulant: " + e.getMessage());
        }
        return "redirect:/flights/" + id;
    }

    @PostMapping("/flights/{id}/crew/{crewId}/edit")
    public String editCrewMember(
            @PathVariable UUID id,
            @PathVariable UUID crewId,
            @ModelAttribute AddCrewMemberForm form,
            RedirectAttributes redirectAttributes) {
        if (isBlank(form.getFirstName()) || isBlank(form.getLastName()) || isBlank(form.getFunction())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nom, cognom i funció són obligatoris.");
            return "redirect:/flights/" + id;
        }
        try {
            updateCrewMember.execute(id, crewId, form);
            redirectAttributes.addFlashAttribute("successMessage", "Tripulant actualitzat.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error en actualitzar tripulant: " + e.getMessage());
        }
        return "redirect:/flights/" + id;
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
