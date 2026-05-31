package com.pau.flight_sync.adapters.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SQLRestriction("deleted_at IS NULL")
@Entity
@Table(name = "flight")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "external_id", nullable = true, unique = true)
    private Long externalId;

    @Column(nullable = false, length = 10)
    private String registration;

    @Column(name = "departure_airport", nullable = false, length = 4)
    private String departureAirport;

    @Column(name = "arrival_airport", nullable = false, length = 4)
    private String arrivalAirport;

    @Column(name = "block_start")
    private LocalDateTime blockStart;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "landing_time", nullable = false)
    private LocalDateTime landingTime;

    @Column(name = "block_end")
    private LocalDateTime blockEnd;

    @Column(name = "distance_km", nullable = false)
    private Integer distanceKm;

    @Column(name = "flight_type", nullable = false, length = 20)
    private String flightType;

    @Column(name = "nb_landings", nullable = false)
    private Integer nbLandings;

    @Column(nullable = false)
    private Boolean night;

    @Column(name = "cross_country", nullable = false)
    private Boolean crossCountry;

    @Column(length = 500)
    private String remarks;

    @Column(name = "deleted_at")
    private java.time.LocalDateTime deletedAt;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CrewMemberEntity> crew = new ArrayList<>();
}
