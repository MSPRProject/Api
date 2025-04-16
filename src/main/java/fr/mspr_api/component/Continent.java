package fr.mspr_api.component;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a continent.")
public enum Continent {
    Europe,
    Asia,
    North_America,
    South_America,
    Africa,
    Oceania,
    Antarctica
}
