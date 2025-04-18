package fr.mspr_api.service;

import fr.mspr_api.component.Report;
import fr.mspr_api.repository.ReportRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);


    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /**
     * Fetch reports by pandemic.
     * @param idPandemic The ID of the pandemic to filter reports.
     * @return List of reports associated with the given pandemic.
     * This method retrieves all reports from the database.
     */
    public List<Report> getReportsByPandemic(Integer idPandemic) {
        return reportRepository.findByPandemicId(idPandemic);
    }

    /**
     * Fetch reports by country.
     * @param idCountry The ID of the country to filter reports.
     * @return List of reports associated with the given country.
     * This method retrieves reports based on the country.
     */
    public List<Report> getReportsByCountry(Integer idCountry) {
        return reportRepository.findByCountryId(idCountry);
    }

    /**
     * Fetch reports by date.
     * @param date The date to filter reports.
     * @return List of reports associated with the given date.
     * This method retrieves reports based on the date.
     */
    public List<Report> getReportsByDate(Timestamp date) {
        return reportRepository.findByDate(date);
    }

    /**
     * Create a new report.
     * @param report The Report object to create.
     * @return The created Report object.
     * This method saves a new report to the database.
     */
    public Report createReport(Report report) {
        return reportRepository.save(report);
    }

    /**
     * Update an existing report.
     * @param id The ID of the report to update.
     * @param updatedReport The Report object with updated data.
     * @throws IllegalArgumentException If the report with the given ID does not exist.
     * @return The updated Report object.
     * This method updates the fields of an existing report based on the provided updatedReport object.
     */
    public Report updateReport(Integer id, Report updatedReport) {
        return reportRepository.findById(id)
            .map(report -> {
                if (updatedReport.getInfection() != null) {
                    logger.info("Updating infection from {} to {}", report.getInfection(), updatedReport.getInfection());
                    report.setInfection(updatedReport.getInfection());
                }
                if (updatedReport.getDate() != null) {
                    logger.info("Updating date from {} to {}", report.getDate(), updatedReport.getDate());
                    report.setDate(updatedReport.getDate());
                }
                if (updatedReport.getNewCases() != null) {
                    logger.info("Updating new cases from {} to {}", report.getNewCases(), updatedReport.getNewCases());
                    report.setNewCases(updatedReport.getNewCases());
                }
                if (updatedReport.getNewDeaths() != null) {
                    logger.info("Updating new deaths from {} to {}", report.getNewDeaths(), updatedReport.getNewDeaths());
                    report.setNewDeaths(updatedReport.getNewDeaths());
                }
                return reportRepository.save(report);
            })
            .orElseThrow(() -> new IllegalArgumentException("Report with ID " + id + " not found."));
    }

    /**
     * Delete a report by its ID.
     * @param id The ID of the report to delete.
     * @throws IllegalArgumentException If the report with the given ID does not exist.
     * @return void
     * This method deletes a report from the database based on its ID.
     */
    public void deleteReport(Integer id) {
        if (reportRepository.existsById(id)) {
            reportRepository.deleteById(id);
            System.out.println("Report with ID " + id + " successfully deleted.");
        } else {
            throw new IllegalArgumentException("Report with ID " + id + " not found.");
        }
    }
}
