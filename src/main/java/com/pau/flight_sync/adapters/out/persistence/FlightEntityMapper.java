package com.pau.flight_sync.adapters.out.persistence;

import com.pau.flight_sync.adapters.out.persistence.entity.CrewMemberEntity;
import com.pau.flight_sync.adapters.out.persistence.entity.FlightEntity;
import com.pau.flight_sync.domain.FlightType;
import com.pau.flight_sync.domain.model.CrewMember;
import com.pau.flight_sync.domain.model.Flight;
import com.pau.flight_sync.domain.valueobject.AirportCode;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
class FlightEntityMapper {

    FlightEntity toEntity(Flight flight) {
        val entity = new FlightEntity();
        entity.setId(flight.getId());
        entity.setExternalId(flight.getExternalId());
        entity.setRegistration(flight.getRegistration());
        entity.setDepartureAirport(flight.getDepartureAirport().code());
        entity.setArrivalAirport(flight.getArrivalAirport().code());
        entity.setBlockStart(flight.getBlockStart());
        entity.setDepartureTime(flight.getDepartureTime());
        entity.setLandingTime(flight.getLandingTime());
        entity.setBlockEnd(flight.getBlockEnd());
        entity.setDistanceKm(flight.getDistanceKm());
        entity.setFlightType(flight.getFlightType().name());
        entity.setNbLandings(flight.getNbLandings() != null ? flight.getNbLandings() : 0);
        entity.setNight(Boolean.TRUE.equals(flight.getNight()));
        entity.setCrossCountry(Boolean.TRUE.equals(flight.getCrossCountry()));
        entity.setRemarks(flight.getRemarks());
        mapCrewToEntities(flight, entity).forEach(entity.getCrew()::add);
        return entity;
    }

    Flight toDomain(FlightEntity entity) {
        val crew = mapCrewToDomain(entity);
        return Flight.builder()
                .id(entity.getId())
                .externalId(entity.getExternalId())
                .registration(entity.getRegistration())
                .departureAirport(new AirportCode(entity.getDepartureAirport()))
                .arrivalAirport(new AirportCode(entity.getArrivalAirport()))
                .blockStart(entity.getBlockStart())
                .departureTime(entity.getDepartureTime())
                .landingTime(entity.getLandingTime())
                .blockEnd(entity.getBlockEnd())
                .distanceKm(entity.getDistanceKm())
                .flightType(FlightType.fromString(entity.getFlightType()))
                .nbLandings(entity.getNbLandings())
                .night(entity.getNight())
                .crossCountry(entity.getCrossCountry())
                .remarks(entity.getRemarks())
                .crew(crew)
                .build();
    }

    private List<CrewMemberEntity> mapCrewToEntities(Flight flight, FlightEntity flightEntity) {
        if (flight.getCrew() == null) return List.of();
        return flight.getCrew().stream()
                .map(member -> toCrewEntity(member, flightEntity))
                .toList();
    }

    private List<CrewMember> mapCrewToDomain(FlightEntity entity) {
        if (entity.getCrew() == null) return List.of();
        return entity.getCrew().stream()
                .map(this::toCrewDomain)
                .toList();
    }

    private CrewMemberEntity toCrewEntity(CrewMember member, FlightEntity flightEntity) {
        return new CrewMemberEntity(UUID.randomUUID(), flightEntity,
                member.getIdUser(), member.getFirstName(), member.getLastName(), member.getFunction());
    }

    private CrewMember toCrewDomain(CrewMemberEntity entity) {
        return new CrewMember(entity.getId(), entity.getIdUser(), entity.getFirstName(),
                entity.getLastName(), entity.getFunc());
    }
}
