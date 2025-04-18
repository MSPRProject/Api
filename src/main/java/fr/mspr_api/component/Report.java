package fr.mspr_api.component;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
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
    name = "Report",
    uniqueConstraints = {@UniqueConstraint(columnNames = {
        "infection_id", 
        "date"
    })}
)
@Schema(description = "Represents a report entity.")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    @Schema(description = "The unique ID of the report.", example = "1")
    private Integer reportId;

    @ManyToOne
    @JoinColumn(name = "infection_id")
    @JsonProperty("infection")
    @Schema(description = "The infection associated with the report.")
    private Infection infection;

    @Column(name = "date")
    @Schema(description = "The date of the report.", example = "2023-04-15T12:00:00Z")
    private Timestamp date;

    @Column(name = "new_cases")
    @JsonProperty("new_cases")
    @Schema(description = "The number of new cases reported.", example = "500")
    private Integer newCases;

    @Column(name = "new_deaths")
    @JsonProperty("new_deaths")
    @Schema(description = "The number of new deaths reported.", example = "20")
    private Integer newDeaths;

    public Report() {
    }

    /**
     * Constructor for Report class.
     * @param infection Infection object representing the infection associated with the report.
     * @param date Timestamp indicating when the report was created.
     * @param newCases Integer representing the number of new cases reported.
     * @param newDeaths Integer representing the number of new deaths reported.
     */
    public Report(Infection infection, Timestamp date, Integer newCases, Integer newDeaths) {
        this.infection = infection;
        this.date = date;
        this.newCases = newCases;
        this.newDeaths = newDeaths;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Infection getInfection() {
        return infection;
    }

    public void setInfection(Infection infection) {
        this.infection = infection;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Integer getNewCases() {
        return newCases;
    }

    public void setNewCases(Integer newCases) {
        this.newCases = newCases;
    }

    public Integer getNewDeaths() {
        return newDeaths;
    }

    public void setNewDeaths(Integer newDeaths) {
        this.newDeaths = newDeaths;
    }

    @Override
    public String toString() {
        return this.infection +
                "Date : " + this.date +
                "New cases :" + this.newCases +
                "New deaths : " + this.newDeaths;
    }
}
