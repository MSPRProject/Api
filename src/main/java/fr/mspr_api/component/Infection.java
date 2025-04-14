package fr.mspr_api.component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "Infection",
    uniqueConstraints = {@UniqueConstraint(columnNames = {
        "country_id", 
        "pandemic_id"
    })}
)
public class Infection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "infection_id")
    private Integer infectionId;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "pandemic_id")
    private Pandemic pandemic;

    @Column(name = "total_cases")
    private Integer totalCases;

    @Column(name = "total_deaths")
    private Integer totalDeaths;

    public Infection() {
    }

    /**
     * Constructor for Infection class.
     * @param infectionId Integer representing the unique identifier for the infection.
     * @param country Country object representing the country affected by the infection.
     * @param pandemic Pandemic object representing the pandemic associated with the infection.
     * @param totalCases Integer representing the total number of cases of the infection.
     * @param totalDeaths Integer representing the total number of deaths caused by the infection.
     */
    public Infection(Integer infectionId, Country country, Pandemic pandemic, Integer totalCases, Integer totalDeaths) {
        this.infectionId = infectionId;
        this.country = country;
        this.pandemic = pandemic;
        this.totalCases = totalCases;
        this.totalDeaths = totalDeaths;
    }

    public Integer getInfectionId() {
        return infectionId;
    }

    public void setInfectionId(Integer infectionId) {
        this.infectionId = infectionId;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Pandemic getPandemic() {
        return pandemic;
    }

    public void setPandemic(Pandemic pandemic) {
        this.pandemic = pandemic;
    }

    public Integer getTotalCases() {
        return totalCases;
    }

    public void setTotalCases(Integer totalCases) {
        this.totalCases = totalCases;
    }

    public Integer getTotalDeaths() {
        return totalCases;
    }

    public void setTotalDeaths(Integer totalDeaths) {
        this.totalCases = totalDeaths;
    }
}
