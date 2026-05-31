package com.pau.flight_sync.adapters.out.jsonimport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PrivateRadarCrewJson(
        @JsonProperty("id_user") Long   idUser,
        String firstname,
        String lastname,
        String func
) {}
