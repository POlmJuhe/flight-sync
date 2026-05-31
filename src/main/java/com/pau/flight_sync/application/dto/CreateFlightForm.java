package com.pau.flight_sync.application.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class CreateFlightForm {

    private String        registration;
    private String        departureAirport;
    private String        arrivalAirport;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime blockStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime departureTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime landingTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime blockEnd;

    private Integer       distanceKm;
    private String        flightType;
    private Integer       nbLandings   = 0;
    private boolean       night        = false;
    private boolean       crossCountry = false;
    private String        remarks;
}
