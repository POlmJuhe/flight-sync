package com.pau.flight_sync.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CrewMember {
    private final UUID   id;
    private final Long   idUser;
    private final String firstName;
    private final String lastName;
    private final String function;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
