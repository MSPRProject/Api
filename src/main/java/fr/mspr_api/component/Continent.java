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
    ANTARCTICA;

    @Override
    public String toString() {
        String lowercase = name().toLowerCase();
        lowercase =
            lowercase.substring(0, 1).toUpperCase() + lowercase.substring(1);
        lowercase = lowercase.replace("_", " ");
        return lowercase;
    }
}
