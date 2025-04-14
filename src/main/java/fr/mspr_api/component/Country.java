package fr.mspr_api.component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Country")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Integer countryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "continent")
    private Continent continent;

    @Column(name = "name")
    private String name;

    @Column(name = "iso3")
    private String iso3;

    @Column(name = "population")
    private Integer population;

    public Country() {
    }

    /**
     * Constructor for Country class.
     * @param continent Continent representing the continent of the country.
     * @param name String representing the name of the country.
     * @param iso3 String representing the ISO 3166-1 alpha-3 code of the country.
     * @param population Integer representing the population of the country.
     */
    public Country(Integer countryId, Continent continent, String name, String iso3, Integer population) {
        this.countryId = countryId;
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
    public Country(Integer countryId, Continent continent, String name, String iso3) {
        this.countryId = countryId;
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
}
