package fr.mspr_api.component;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Country")
@Schema(description = "Represents a country entity.")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    @JsonProperty("id")
    @Schema(description = "The unique ID of the country.", example = "1")
    private Integer countryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "continent", length = 20)
    @Schema(description = "The continent where the country is located.", example = "EUROPE")
    private Continent continent;

    @Column(name = "name", length = 50)
    @Schema(description = "The name of the country.", example = "France")
    private String name;

    @Column(name = "iso3", length = 3)
    @Schema(description = "The ISO 3166-1 alpha-3 code of the country.", example = "FRA")
    private String iso3;

    @Column(name = "population")
    @Schema(description = "The population of the country.", example = "67000000")
    private Integer population;

    @OneToMany(
        mappedBy = "country",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @Schema(description = "The list of infections associated with the country.")
    private List<Infection> infections;

    public Country() {
    }

    /**
     * Constructor for Country class.
     * @param continent Continent representing the continent of the country.
     * @param name String representing the name of the country.
     * @param iso3 String representing the ISO 3166-1 alpha-3 code of the country.
     * @param population Integer representing the population of the country.
     */
    public Country(Continent continent, String name, String iso3, Integer population) {
        this.continent = continent;
        this.name = name;
        this.iso3 = iso3;
        this.population = population;
    }

    /**
     * Constructor for Country class without population.
     * @param continent
     * @param name
     * @param iso3
     */
    public Country(Continent continent, String name, String iso3) {
        this.continent = continent;
        this.name = name;
        this.iso3 = iso3;
        this.population = null;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Continent getContinent() {
        return continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    @Override
    public String toString() {
        return this.name + " (" + this.iso3 + ")";
    }
}
