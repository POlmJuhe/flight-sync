package com.pau.flight_sync.domain.repository;

import com.pau.flight_sync.domain.FlightStats;
import com.pau.flight_sync.domain.model.CrewMember;
import com.pau.flight_sync.domain.model.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FlightRepositoryPort {
    boolean existsByExternalId(Long externalId);
    void save(Flight flight);
    void addCrewMember(UUID flightId, CrewMember member);
    Page<Flight> findAll(Pageable pageable);
    Page<Flight> findFiltered(String registration, String flightType, Integer year, String airport, Pageable pageable);
    List<String>  findDistinctRegistrations();
    List<Integer> findDistinctYears();
    Optional<Flight> findById(UUID id);
    FlightStats getStats();
    FlightStats getFilteredStats(String registration, String flightType, Integer year, String airport);
    void softDeleteFlight(UUID id);
    void updateFlight(UUID id, String registration, String flightType,
                      String departureAirport, String arrivalAirport,
                      LocalDateTime blockStart, LocalDateTime departureTime,
                      LocalDateTime landingTime, LocalDateTime blockEnd,
                      Integer distanceKm, Integer nbLandings,
                      boolean night, boolean crossCountry, String remarks);
    void removeCrewMember(UUID flightId, UUID crewMemberId);
    void updateCrewMember(UUID flightId, UUID crewMemberId,
                          String firstName, String lastName, String function);
    java.time.LocalDateTime findFirstFlightDate();
    long countByFlightType(String type);
    String findTopAirport();
}
