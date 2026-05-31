package com.pau.flight_sync.adapters.out.jsonimport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public record PrivateRadarFlightJson(
        Long   id,
        String registration,
        @JsonProperty("from")          String        from,
        @JsonProperty("to")            String        to,
        @JsonProperty("flight_type")   String        flightType,
        @JsonProperty("dist_km")       Integer       distKm,
        @JsonProperty("dt_start")      LocalDateTime dtStart,
        @JsonProperty("dt_takeoff")    LocalDateTime dtTakeoff,
        @JsonProperty("dt_landing")    LocalDateTime dtLanding,
        @JsonProperty("dt_terminated") LocalDateTime dtTerminated,
        @JsonProperty("nb_landings")   Integer       nbLandings,
        Boolean                        night,
        Boolean                        xcountry,
        String                         remarks,
        List<PrivateRadarCrewJson>     crew
) {}
