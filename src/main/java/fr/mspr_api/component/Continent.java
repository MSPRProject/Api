package fr.mspr_api.component;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a continent.")
public enum Continent {
    EUROPE,
    ASIA,
    NORTH_AMERICA,
    SOUTH_AMERICA,
    AFRICA,
    OCEANIA,
    ANTARCTICA
}
