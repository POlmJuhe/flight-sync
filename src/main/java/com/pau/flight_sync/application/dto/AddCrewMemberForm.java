package com.pau.flight_sync.application.dto;

import lombok.Data;

@Data
public class AddCrewMemberForm {
    private String firstName;
    private String lastName;
    private String function;
}
