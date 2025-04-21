package fr.mspr_api.controller;

import fr.mspr_api.service.ChartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/charts")
public class ChartController {

    @Autowired
    private ChartService chartService;

    @Operation(
        summary = "Get infection distribution by continent",
        description = "Generates a pie chart showing infection distribution by continent for a given pandemic."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Chart generated successfully",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Pandemic not found"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            )
        }
    )
    @GetMapping("/infectionDistributionByContinent")
    public ResponseEntity<?> getInfectionDistributionByContinent(Integer pandemicId) {
        try {
            String chartJson = chartService.getInfectionDistributionByContinent(pandemicId);
            return ResponseEntity.ok(chartJson);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating chart: " + e.getMessage());
        }
    }

    @Operation(
        summary = "Get new cases and deaths over time",
        description = "Generates a line chart showing new cases and deaths over time for a specific country and pandemic."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Chart generated successfully",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Data not found"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            )
        }
    )
    @GetMapping("/newCasesDeathsOverTime")
    public ResponseEntity<?> getNewCasesDeathsOverTime(Integer countryId, Integer pandemicId) {
        try {
            String chartJson = chartService.getNewCasesDeathsOverTime(countryId, pandemicId);
            return ResponseEntity.ok(chartJson);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating chart: " + e.getMessage());
        }
    }

    @Operation(
        summary = "Prune all cached charts",
        description = "Deletes all cached chart files and clears the cache."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "All caches cleared successfully",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error occurred while clearing caches",
                content = @Content(mediaType = "application/json")
            ),
        }
    )
    @GetMapping("/pruneCaches")
    public ResponseEntity<?> pruneCaches() {
        try {
            chartService.pruneCaches();
            return ResponseEntity.ok("{\"message\": \"All caches cleared successfully.\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Error occurred while clearing caches.\"}");
        }
    }
}