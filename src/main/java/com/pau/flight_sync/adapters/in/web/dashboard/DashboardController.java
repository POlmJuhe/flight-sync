package com.pau.flight_sync.adapters.in.web.dashboard;

import com.pau.flight_sync.application.dto.CreateFlightForm;
import com.pau.flight_sync.application.usecase.flight.GetAirportsUseCase;
import com.pau.flight_sync.application.usecase.flight.GetDashboardUseCase;
import com.pau.flight_sync.domain.FlightType;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final GetDashboardUseCase getDashboard;
    private final GetAirportsUseCase  getAirports;

    @GetMapping("/")
    public String dashboard(
            @RequestParam(defaultValue = "0") int     page,
            @RequestParam(required = false)   String  registration,
            @RequestParam(required = false)   String  flightType,
            @RequestParam(required = false)   Integer year,
            @RequestParam(required = false)   String  airport,
            Model model) {

        registration = blankToNull(registration);
        flightType   = blankToNull(flightType);
        airport      = blankToNull(airport);

        val view = getDashboard.execute(page, registration, flightType, year, airport);

        model.addAttribute("stats",               view.stats());
        model.addAttribute("flights",             view.flights());
        model.addAttribute("distinctRegistrations", view.registrations());
        model.addAttribute("distinctYears",         view.years());
        model.addAttribute("form",        new CreateFlightForm());
        model.addAttribute("flightTypes", List.of(FlightType.INSTR, FlightType.SOLO));
        model.addAttribute("airports",    getAirports.execute());
        model.addAttribute("currentRegistration", registration);
        model.addAttribute("currentFlightType",   flightType);
        model.addAttribute("currentYear",         year);
        model.addAttribute("currentAirport",      airport);
        return "dashboard";
    }

    private String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}
