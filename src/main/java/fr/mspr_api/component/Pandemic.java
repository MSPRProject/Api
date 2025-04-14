package fr.mspr_api.component;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Pandemic")
public class Pandemic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pandemic_id")
    private Integer pandemicId;

    @Column(name = "name")
    private String name;

    @Column(name = "pathogen")
    private String pathogen;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "end_date")
    private Timestamp endDate;

    @Column(name = "notes")
    private String notes;

    public Pandemic() {
    }

    /**
     * Constructor for Pandemic class.
     * @param pandemicId Integer representing the unique identifier for the pandemic.
     * @param name String representing the name of the pandemic.
     * @param pathogen String representing the pathogen responsible for the pandemic.
     * @param description String providing a description of the pandemic.
     * @param startDate Timestamp indicating when the pandemic started.
     * @param endDate Timestamp indicating when the pandemic ended.
     * @param notes String containing additional notes about the pandemic.
     */ 
    public Pandemic(Integer pandemicId, String name, String pathogen, String description, Timestamp startDate, Timestamp endDate, String notes) {
        this.pandemicId = pandemicId;
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
}
