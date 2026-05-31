package com.pau.flight_sync.adapters.out.jsonimport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PrivateRadarExportJson(
        @JsonProperty("flight_list") List<PrivateRadarFlightJson> flightList
) {}
