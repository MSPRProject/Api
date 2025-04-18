package fr.mspr_api.controller;

import fr.mspr_api.component.Report;
import fr.mspr_api.service.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Get all reports by pandemic.
     * @param idPandemic The ID of the pandemic.
     * @return List of reports associated with the given pandemic.
     * This method retrieves reports based on the pandemic.
     */
    @Operation(summary = "Get reports by pandemic", description = "Retrieve all reports associated with a specific pandemic ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reports retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Pandemic not found")
    })
    @GetMapping("/pandemics/{id_pandemic}")
    public List<Report> getReportsByPandemic(@PathVariable("id_pandemic") @Schema(description = "Pandemic ID") Integer idPandemic) {
        return reportService.getReportsByPandemic(idPandemic);
    }

    /**
     * Get all reports by country.
     * @param idCountry The ID of the country.
     * @return List of reports associated with the given country.
     * This method retrieves reports based on the country.
     */
    @Operation(summary = "Create a new report", description = "Create a new report for a specific infection.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Report created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @GetMapping("/countries/{id_country}")
    public List<Report> getReportsByCountry(@PathVariable("id_country") @Schema(description = "Country ID") Integer idCountry) {
        return reportService.getReportsByCountry(idCountry);
    }

    /**
     * Get all reports by date.
     * @param date The date to filter reports.
     * @return List of reports associated with the given date.
     * This method retrieves reports based on the date.
     */
    @Operation(summary = "Get reports by country", description = "Retrieve all reports associated with a specific country ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reports retrieved successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Report.class))),
        @ApiResponse(responseCode = "404", description = "Country not found")
    })
    @GetMapping("/{date}")
    public List<Report> getReportsByDate(@PathVariable("date")@Schema(description = "Date in ISO format") Timestamp date) {
        return reportService.getReportsByDate(date);
    }

    /**
     * Create a new report.
     * @param report The Report object to create.
     * @return The created Report object.
     * This method saves a new report to the database.
     */
    @Operation(summary = "Create a new report", description = "Create a new report for a specific infection.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Report created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public Report createReport(@RequestBody @Schema(description = "Report object") Report report) {
        return reportService.createReport(report);
    }

    /**
     * Update an existing report.
     * @param id The ID of the report to update.
     * @param updatedReport The Report object with updated data.
     * @return The updated Report object.
     * This method updates the fields of an existing report based on the provided updatedReport object.
     */
    @Operation(summary = "Update an existing report", description = "Update the fields of an existing report.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Report updated successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Report.class))),
        @ApiResponse(responseCode = "404", description = "Report not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PatchMapping("/{id}")
    public Report updateReport(@PathVariable("id") @Schema(description = "Report ID") Integer id, 
                               @RequestBody @Schema(description = "Updated report object") Report updatedReport) {
        return reportService.updateReport(id, updatedReport);
    }

    /**
     * Delete a report by ID.
     * @param id The ID of the report to delete.
     * @return void
     * This method deletes a report from the database based on the provided ID.
     */
    @Operation(summary = "Delete a report by ID", description = "Delete a report from the database based on its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Report deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Report not found")
    })
    @DeleteMapping("/{id}")
    public void deleteReport(@PathVariable("id") @Schema(description = "Report ID") Integer id) {
        reportService.deleteReport(id);
    }
}
