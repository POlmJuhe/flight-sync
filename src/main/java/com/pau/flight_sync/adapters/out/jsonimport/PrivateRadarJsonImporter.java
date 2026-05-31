package com.pau.flight_sync.adapters.out.jsonimport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pau.flight_sync.adapters.out.jsonimport.dto.PrivateRadarCrewJson;
import com.pau.flight_sync.adapters.out.jsonimport.dto.PrivateRadarExportJson;
import com.pau.flight_sync.adapters.out.jsonimport.dto.PrivateRadarFlightJson;
import com.pau.flight_sync.domain.FlightType;
import com.pau.flight_sync.domain.model.CrewMember;
import com.pau.flight_sync.domain.model.Flight;
import com.pau.flight_sync.domain.port.FlightJsonParserPort;
import com.pau.flight_sync.domain.valueobject.AirportCode;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PrivateRadarJsonImporter implements FlightJsonParserPort {

    private final ObjectMapper objectMapper;

    @Override
    public List<Flight> parse(InputStream stream) throws IOException {
        val export = objectMapper.readValue(stream, PrivateRadarExportJson.class);
        if (export.flightList() == null) return List.of();
        return export.flightList().stream().map(this::toDomain).toList();
    }

    private Flight toDomain(PrivateRadarFlightJson json) {
        return Flight.builder()
                .id(UUID.randomUUID())
                .externalId(json.id())
                .registration(json.registration())
                .departureAirport(new AirportCode(json.from()))
                .arrivalAirport(new AirportCode(json.to()))
                .blockStart(json.dtStart())
                .departureTime(json.dtTakeoff())
                .landingTime(json.dtLanding())
                .blockEnd(json.dtTerminated())
                .distanceKm(json.distKm() != null ? json.distKm() : 0)
                .flightType(FlightType.fromString(json.flightType()))
                .nbLandings(json.nbLandings() != null ? json.nbLandings() : 0)
                .night(Boolean.TRUE.equals(json.night()))
                .crossCountry(Boolean.TRUE.equals(json.xcountry()))
                .remarks(json.remarks() != null ? json.remarks().strip() : null)
                .crew(mapCrew(json.crew()))
                .build();
    }

    private List<CrewMember> mapCrew(List<PrivateRadarCrewJson> crew) {
        if (crew == null) return List.of();
        return crew.stream().map(this::toCrewDomain).toList();
    }

    private CrewMember toCrewDomain(PrivateRadarCrewJson json) {
        return new CrewMember(null, json.idUser(), json.firstname(), json.lastname(), json.func());
    }
}
