package com.pau.flight_sync.domain;

public enum FlightType {
    INSTR("Con instructor"),
    SOLO("Solo"),
    UNKNOWN("Desconocido");

    private final String label;

    FlightType(String label) { this.label = label; }

    public String getLabel() { return label; }

    public static FlightType fromString(String value) {
        if (value == null) return UNKNOWN;
        return switch (value.toLowerCase()) {
            case "instr" -> INSTR;
            case "solo"  -> SOLO;
            default      -> UNKNOWN;
        };
    }
}
