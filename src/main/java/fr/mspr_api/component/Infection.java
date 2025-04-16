package fr.mspr_api.component;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(
    name = "Infection",
    uniqueConstraints = {@UniqueConstraint(columnNames = {
        "country_id", 
        "pandemic_id"
    })}
)
@Schema(description = "Represents an infection entity.")
public class Infection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "infection_id")
    @JsonProperty("id")
    @Schema(description = "The unique ID of the infection.", example = "1")
    private Integer infectionId;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    @NotNull(message = "Country cannot be null")
    @JsonProperty("country")
    @Schema(description = "The country affected by the infection.")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "pandemic_id", nullable = false)
    @NotNull(message = "Pandemic cannot be null")
    @JsonProperty("pandemic")
    @Schema(description = "The pandemic associated with the infection.")
    private Pandemic pandemic;

    @Column(name = "total_cases")
    @Schema(description = "The total number of cases of the infection.", example = "100000")
    @JsonProperty("total_cases")
    private Integer totalCases;

    @Column(name = "total_deaths")
    @Schema(description = "The total number of deaths caused by the infection.", example = "5000")
    @JsonProperty("total_deaths")
    private Integer totalDeaths;

    @OneToMany(
        mappedBy = "infection",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @Schema(description = "The list of reports associated with the infection.")
    private List<Report> reports;

    public Infection() {
    }

    /**
     * Constructor for Infection class.
     * @param country Country object representing the country affected by the infection.
     * @param pandemic Pandemic object representing the pandemic associated with the infection.
     * @param totalCases Integer representing the total number of cases of the infection.
     * @param totalDeaths Integer representing the total number of deaths caused by the infection.
     */
    public Infection(Country country, Pandemic pandemic, Integer totalCases, Integer totalDeaths) {
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
        return totalDeaths;
    }

    public void setTotalDeaths(Integer totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    @Override
    public String toString() {
        return this.pandemic.getName() + " in " + this.country + " : " + this.totalCases + " cases and " + this.totalDeaths + " deaths";
    }
}
