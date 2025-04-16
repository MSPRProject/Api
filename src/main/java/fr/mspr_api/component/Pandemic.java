package fr.mspr_api.component;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Pandemic")
@Schema(description = "Represents a pandemic entity.")
public class Pandemic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pandemic_id")
    @JsonProperty("id")
    @Schema(description = "The unique ID of the pandemic.", example = "1")
    private Integer pandemicId;

    @Column(name = "name")
    @Schema(description = "The name of the pandemic.", example = "COVID-19")
    private String name;

    @Column(name = "pathogen")
    @Schema(description = "The pathogen responsible for the pandemic.", example = "SARS-CoV-2")
    private String pathogen;

    @Column(name = "description")
    @Schema(description = "A description of the pandemic.", example = "A global pandemic caused by the SARS-CoV-2 virus.")
    private String description;

    @Column(name = "start_date")
    @JsonProperty("start_date")
    @Schema(description = "The start date of the pandemic.", example = "2020-01-01T00:00:00Z")
    private Timestamp startDate;

    @Column(name = "end_date")
    @JsonProperty("end_date")
    @Schema(description = "The end date of the pandemic.", example = "2022-12-31T23:59:59Z")
    private Timestamp endDate;

    @Column(name = "notes")
    @Schema(description = "Additional notes about the pandemic.", example = "Vaccination campaigns were launched globally.")
    private String notes;

    @OneToMany(
        mappedBy = "pandemic",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @Schema(description = "The list of infections associated with the pandemic.")
    private List<Infection> infections;

    public Pandemic() {
    }

    /**
     * Constructor for Pandemic class.
     * @param name String representing the name of the pandemic.
     * @param pathogen String representing the pathogen responsible for the pandemic.
     * @param description String providing a description of the pandemic.
     * @param startDate Timestamp indicating when the pandemic started.
     * @param endDate Timestamp indicating when the pandemic ended.
     * @param notes String containing additional notes about the pandemic.
     */ 
    public Pandemic(String name, String pathogen, String description, Timestamp startDate, Timestamp endDate, String notes) {
        this.name = name;
        this.pathogen = pathogen;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
    }

    public Integer getPandemicId() {
        return pandemicId;
    }

    public void setPandemicId(Integer pandemicId) {
        this.pandemicId = pandemicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathogen() {
        return pathogen;
    }

    public void setPathogen(String pathogen) {
        this.pathogen = pathogen;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return this.name + " (" + this.pathogen + ")";
    }
}
