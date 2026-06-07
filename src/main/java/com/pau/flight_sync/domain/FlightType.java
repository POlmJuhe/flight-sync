package com.pau.flight_sync.domain;

public enum FlightType {
    INSTR("Amb instructor"),
    SOLO("Sol"),
    UNKNOWN("Desconegut");

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
