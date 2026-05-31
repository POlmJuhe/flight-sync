package com.pau.flight_sync.adapters.out.persistence;

import com.pau.flight_sync.adapters.out.persistence.entity.CrewMemberEntity;
import com.pau.flight_sync.domain.FlightStats;
import com.pau.flight_sync.domain.model.CrewMember;
import com.pau.flight_sync.domain.model.Flight;
import com.pau.flight_sync.domain.repository.FlightRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FlightPersistenceAdapter implements FlightRepositoryPort {

    private final FlightJpaRepository jpaRepository;
    private final FlightEntityMapper  mapper;

    @Override
    public boolean existsByExternalId(Long externalId) {
        return jpaRepository.existsByExternalId(externalId);
    }

    @Override
    @Transactional
    public void save(Flight flight) {
        jpaRepository.save(mapper.toEntity(flight));
    }

    @Override
    @Transactional
    public void addCrewMember(UUID flightId, CrewMember member) {
        val flightEntity = jpaRepository.findById(flightId).orElseThrow(
                () -> new IllegalArgumentException("Flight not found: " + flightId));
        val crewEntity = new CrewMemberEntity(
                UUID.randomUUID(), flightEntity,
                member.getIdUser(), member.getFirstName(), member.getLastName(), member.getFunction());
        flightEntity.getCrew().add(crewEntity);
        jpaRepository.save(flightEntity);
    }

    @Override
    public Page<Flight> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public Page<Flight> findFiltered(String registration, String flightType, Integer year, String airport, Pageable pageable) {
        return jpaRepository.findFiltered(registration, flightType, year, airport, pageable).map(mapper::toDomain);
    }

    @Override
    public List<String> findDistinctRegistrations() {
        return jpaRepository.findDistinctRegistrations();
    }

    @Override
    public List<Integer> findDistinctYears() {
        return jpaRepository.findDistinctYears();
    }

    @Override
    public Optional<Flight> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void softDeleteFlight(UUID id) {
        jpaRepository.findById(id).ifPresent(entity -> {
            entity.setDeletedAt(LocalDateTime.now());
            jpaRepository.save(entity);
        });
    }

    @Override
    @Transactional
    public void updateFlight(UUID id, String registration, String flightType,
                             String departureAirport, String arrivalAirport,
                             LocalDateTime blockStart, LocalDateTime departureTime,
                             LocalDateTime landingTime, LocalDateTime blockEnd,
                             Integer distanceKm, Integer nbLandings,
                             boolean night, boolean crossCountry, String remarks) {
        jpaRepository.findById(id).ifPresent(entity -> {
            entity.setRegistration(registration);
            entity.setFlightType(flightType);
            entity.setDepartureAirport(departureAirport);
            entity.setArrivalAirport(arrivalAirport);
            entity.setBlockStart(blockStart);
            entity.setDepartureTime(departureTime);
            entity.setLandingTime(landingTime);
            entity.setBlockEnd(blockEnd);
            entity.setDistanceKm(distanceKm != null ? distanceKm : 0);
            entity.setNbLandings(nbLandings != null ? nbLandings : 0);
            entity.setNight(night);
            entity.setCrossCountry(crossCountry);
            entity.setRemarks(remarks != null ? remarks.strip() : null);
            jpaRepository.save(entity);
        });
    }

    @Override
    @Transactional
    public void removeCrewMember(UUID flightId, UUID crewMemberId) {
        jpaRepository.findById(flightId).ifPresent(entity -> {
            entity.getCrew().removeIf(c -> c.getId().equals(crewMemberId));
            jpaRepository.save(entity);
        });
    }

    @Override
    public java.time.LocalDateTime findFirstFlightDate() {
        return jpaRepository.findFirstFlightDate();
    }

    @Override
    public long countByFlightType(String type) {
        return jpaRepository.countByFlightType(type);
    }

    @Override
    public String findTopAirport() {
        return jpaRepository.findTopAirport();
    }

    @Override
    @Transactional
    public void updateCrewMember(UUID flightId, UUID crewMemberId,
                                 String firstName, String lastName, String function) {
        jpaRepository.findById(flightId).ifPresent(entity ->
                entity.getCrew().stream()
                        .filter(c -> c.getId().equals(crewMemberId))
                        .findFirst()
                        .ifPresent(c -> {
                            c.setFirstName(firstName);
                            c.setLastName(lastName);
                            c.setFunc(function);
                        })
        );
    }

    @Override
    public FlightStats getStats() {
        val totalFlights    = jpaRepository.count();
        val rawMinutes      = jpaRepository.sumFlightTimeMinutes();
        val rawDistanceKm   = jpaRepository.sumDistanceKm();
        val mostUsedRaw     = jpaRepository.findMostUsedAircraft();

        return new FlightStats(
                totalFlights,
                rawMinutes    != null ? rawMinutes.longValue()    : 0L,
                rawDistanceKm != null ? rawDistanceKm.longValue() : 0L,
                mostUsedRaw   != null ? mostUsedRaw               : "-");
    }

    @Override
    public FlightStats getFilteredStats(String registration, String flightType, Integer year, String airport) {
        val totalFlights  = jpaRepository.countFiltered(registration, flightType, year, airport);
        val rawMinutes    = jpaRepository.sumFlightTimeMinutesFiltered(registration, flightType, year, airport);
        val rawDistanceKm = jpaRepository.sumDistanceKmFiltered(registration, flightType, year, airport);
        val mostUsedRaw   = jpaRepository.findMostUsedAircraftFiltered(registration, flightType, year, airport);

        return new FlightStats(
                totalFlights,
                rawMinutes    != null ? rawMinutes.longValue()    : 0L,
                rawDistanceKm != null ? rawDistanceKm.longValue() : 0L,
                mostUsedRaw   != null ? mostUsedRaw               : "-");
    }
}
