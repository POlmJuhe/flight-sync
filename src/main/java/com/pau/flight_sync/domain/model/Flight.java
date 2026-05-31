package com.pau.flight_sync.domain.model;

import com.pau.flight_sync.domain.FlightType;
import com.pau.flight_sync.domain.valueobject.AirportCode;
import com.pau.flight_sync.domain.valueobject.FlightDuration;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Builder
public class Flight {
    private UUID          id;
    private Long          externalId;
    private String        registration;
    private AirportCode   departureAirport;
    private AirportCode   arrivalAirport;
    private LocalDateTime blockStart;
    private LocalDateTime departureTime;
    private LocalDateTime landingTime;
    private LocalDateTime blockEnd;
    private Integer       distanceKm;
    private FlightType    flightType;
    private Integer       nbLandings;
    private Boolean       night;
    private Boolean       crossCountry;
    private String        remarks;
    private List<CrewMember> crew;

    public FlightDuration getDuration() {
        return FlightDuration.between(departureTime, landingTime);
    }

    public FlightDuration getBlockTime() {
        if (blockStart == null || blockEnd == null) return getDuration();
        return FlightDuration.between(blockStart, blockEnd);
    }

    public String getRoute() {
        return departureAirport + " → " + arrivalAirport;
    }

    public Optional<CrewMember> getPic() {
        if (crew == null) return Optional.empty();
        return crew.stream()
                .filter(c -> "PIC".equalsIgnoreCase(c.getFunction()))
                .findFirst();
    }
}
