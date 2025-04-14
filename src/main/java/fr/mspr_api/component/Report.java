package fr.mspr_api.component;

import java.sql.Timestamp;

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
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer reportId;

    @ManyToOne
    @JoinColumn(name = "infection_id")
    private Infection infection;

    @Column(name = "date")
    private Timestamp date;
    
    @Column(name = "new_cases")
    private Integer newCases;

    @Column(name = "new_deaths")
    private Integer newDeaths;

    public Report() {
    }

    /**
     * Constructor for Report class.
     * @param reportId Integer representing the unique identifier for the report.
     * @param infection Infection object representing the infection associated with the report.
     * @param date Timestamp indicating when the report was created.
     * @param newCases Integer representing the number of new cases reported.
     * @param newDeaths Integer representing the number of new deaths reported.
     */
    public Report(Integer reportId, Infection infection, Timestamp date, Integer newCases, Integer newDeaths) {
        this.reportId = reportId;
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
}
