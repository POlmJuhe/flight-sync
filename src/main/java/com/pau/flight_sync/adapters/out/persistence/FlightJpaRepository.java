package com.pau.flight_sync.adapters.out.persistence;

import com.pau.flight_sync.adapters.out.persistence.entity.FlightEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

interface FlightJpaRepository extends JpaRepository<FlightEntity, UUID> {

    boolean existsByExternalId(Long externalId);

    @Query("SELECT f FROM FlightEntity f WHERE " +
           "(:registration IS NULL OR f.registration = :registration) AND " +
           "(:flightType IS NULL OR f.flightType = :flightType) AND " +
           "(:year IS NULL OR YEAR(f.departureTime) = :year) AND " +
           "(:airport IS NULL OR f.departureAirport = :airport OR f.arrivalAirport = :airport)")
    Page<FlightEntity> findFiltered(
            @Param("registration") String registration,
            @Param("flightType")   String flightType,
            @Param("year")         Integer year,
            @Param("airport")      String airport,
            Pageable pageable);

    @Query("SELECT DISTINCT f.registration FROM FlightEntity f ORDER BY f.registration")
    List<String> findDistinctRegistrations();

    @Query(value = "SELECT DISTINCT EXTRACT(YEAR FROM departure_time)::int " +
                   "FROM flight WHERE deleted_at IS NULL ORDER BY 1 DESC",
           nativeQuery = true)
    List<Integer> findDistinctYears();

    @Query(value = "SELECT COUNT(*) FROM flight WHERE deleted_at IS NULL " +
                   "AND (:registration IS NULL OR registration = :registration) " +
                   "AND (:flightType   IS NULL OR flight_type  = :flightType)   " +
                   "AND (:year         IS NULL OR EXTRACT(YEAR FROM departure_time) = :year) " +
                   "AND (:airport      IS NULL OR departure_airport = :airport OR arrival_airport = :airport)",
           nativeQuery = true)
    long countFiltered(@Param("registration") String registration,
                       @Param("flightType")   String flightType,
                       @Param("year")         Integer year,
                       @Param("airport")      String airport);

    @Query(value = "SELECT COALESCE(SUM(EXTRACT(EPOCH FROM (landing_time - departure_time)) / 60), 0) " +
                   "FROM flight WHERE deleted_at IS NULL " +
                   "AND (:registration IS NULL OR registration = :registration) " +
                   "AND (:flightType   IS NULL OR flight_type  = :flightType)   " +
                   "AND (:year         IS NULL OR EXTRACT(YEAR FROM departure_time) = :year) " +
                   "AND (:airport      IS NULL OR departure_airport = :airport OR arrival_airport = :airport)",
           nativeQuery = true)
    BigDecimal sumFlightTimeMinutesFiltered(@Param("registration") String registration,
                                            @Param("flightType")   String flightType,
                                            @Param("year")         Integer year,
                                            @Param("airport")      String airport);

    @Query(value = "SELECT COALESCE(SUM(distance_km), 0) FROM flight WHERE deleted_at IS NULL " +
                   "AND (:registration IS NULL OR registration = :registration) " +
                   "AND (:flightType   IS NULL OR flight_type  = :flightType)   " +
                   "AND (:year         IS NULL OR EXTRACT(YEAR FROM departure_time) = :year) " +
                   "AND (:airport      IS NULL OR departure_airport = :airport OR arrival_airport = :airport)",
           nativeQuery = true)
    BigDecimal sumDistanceKmFiltered(@Param("registration") String registration,
                                     @Param("flightType")   String flightType,
                                     @Param("year")         Integer year,
                                     @Param("airport")      String airport);

    @Query(value = "SELECT registration FROM flight WHERE deleted_at IS NULL " +
                   "AND (:registration IS NULL OR registration = :registration) " +
                   "AND (:flightType   IS NULL OR flight_type  = :flightType)   " +
                   "AND (:year         IS NULL OR EXTRACT(YEAR FROM departure_time) = :year) " +
                   "AND (:airport      IS NULL OR departure_airport = :airport OR arrival_airport = :airport) " +
                   "GROUP BY registration ORDER BY COUNT(*) DESC LIMIT 1",
           nativeQuery = true)
    String findMostUsedAircraftFiltered(@Param("registration") String registration,
                                        @Param("flightType")   String flightType,
                                        @Param("year")         Integer year,
                                        @Param("airport")      String airport);

    @Query(value = "SELECT registration FROM flight WHERE deleted_at IS NULL GROUP BY registration ORDER BY COUNT(*) DESC LIMIT 1",
            nativeQuery = true)
    String findMostUsedAircraft();

    @Query(value = "SELECT COALESCE(SUM(EXTRACT(EPOCH FROM (landing_time - departure_time)) / 60), 0) FROM flight WHERE deleted_at IS NULL",
            nativeQuery = true)
    BigDecimal sumFlightTimeMinutes();

    @Query(value = "SELECT COALESCE(SUM(distance_km), 0) FROM flight WHERE deleted_at IS NULL",
            nativeQuery = true)
    BigDecimal sumDistanceKm();

    @Query(value = "SELECT MIN(departure_time) FROM flight WHERE deleted_at IS NULL", nativeQuery = true)
    java.time.LocalDateTime findFirstFlightDate();

    @Query(value = "SELECT COUNT(*) FROM flight WHERE deleted_at IS NULL AND flight_type = :type", nativeQuery = true)
    long countByFlightType(@Param("type") String type);

    @Query(value = """
            SELECT airport FROM (
                SELECT departure_airport AS airport FROM flight WHERE deleted_at IS NULL
                UNION ALL
                SELECT arrival_airport   AS airport FROM flight WHERE deleted_at IS NULL
            ) sub GROUP BY airport ORDER BY COUNT(*) DESC LIMIT 1
            """, nativeQuery = true)
    String findTopAirport();
}
