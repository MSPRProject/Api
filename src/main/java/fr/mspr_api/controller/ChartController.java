package fr.mspr_api.controller;

import fr.mspr_api.component.Country;
import fr.mspr_api.component.Pandemic;
import fr.mspr_api.repository.CountryRepository;
import fr.mspr_api.repository.PandemicRepository;
import fr.mspr_api.service.ChartService;
import fr.mspr_api.service.ChartService.ChartGeneratingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.Optional;
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

    @Autowired
    private PandemicRepository pandemicRepository;

    @Autowired
    private CountryRepository countryRepository;

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
                responseCode = "202",
                description = "Chart generation in progress",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Pandemic not found"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            ),
        }
    )
    @GetMapping("/infectionDistributionByContinent")
    public ResponseEntity<?> getInfectionDistributionByContinent(
        @Schema(
            description = "The pandemic to filter by (either a JSON object with minimally {pandemic_id: number} or a link to the pandemic)",
            anyOf = { Pandemic.class, String.class }
        ) Pandemic pandemic
    ) {
        // The objects received are transient, so we need to fetch the pandemic from the database
        pandemic = pandemicRepository.findById(pandemic.getId()).orElseThrow();

        try {
            String chartJson = chartService.getInfectionDistributionByContinent(
                pandemic
            );
            return ResponseEntity.ok(chartJson);
        } catch (ChartGeneratingException e) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                e.getMessage()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                "Error generating chart: " + e.getMessage()
            );
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
                responseCode = "202",
                description = "Chart generation in progress",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            ),
        }
    )
    @GetMapping("/newCasesDeathsOverTime")
    public ResponseEntity<?> getNewCasesDeathsOverTime(
        @Schema(
            description = "The country to filter by (either a JSON object with minimally {country_id: number}, or a link to the country)",
            anyOf = { Country.class, String.class }
        ) Country country,
        @Schema(
            description = "The pandemic to filter by (either a JSON object with minimally {pandemic_id: number}, or a link to the pandemic)",
            anyOf = { Pandemic.class, String.class }
        ) Pandemic pandemic
    ) {
        // The objects received are transient, so we need to fetch them from the database
        country = countryRepository.findById(country.getId()).orElseThrow();
        pandemic = pandemicRepository.findById(pandemic.getId()).orElseThrow();

        try {
            String chartJson = chartService.getNewCasesDeathsOverTime(
                country,
                pandemic
            );
            return ResponseEntity.ok(chartJson);
        } catch (ChartGeneratingException e) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                e.getMessage()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                e.getMessage()
            );
        }
    }

    @Operation(
        summary = "Get chart data",
        description = "Bar chart representing total cases and deaths by country and pandemic."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Chart generated successfully",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "202",
                description = "Chart generation in progress",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            ),
        }
    )
    @GetMapping("/totalCasesDeathsByCountryAndPandemic")
    public ResponseEntity<String> getTotalCasesDeathsByCountryAndPandemic(
        @Schema(
            description = "Country (either a JSON object with minimally {country_id: number} or a link to the country)",
            anyOf = { Country.class, String.class }
        ) Optional<Country> country,
        @Schema(
            description = "Pandemic (either a JSON object with minimally {pandemic_id: number} or a link to the pandemic)",
            anyOf = { Pandemic.class, String.class }
        ) Optional<Pandemic> pandemic
    ) {
        // The objects received are transient, so we need to fetch them from the database
        country = country.map(c ->
            countryRepository.findById(c.getId()).orElseThrow()
        );
        pandemic = pandemic.map(p ->
            pandemicRepository.findById(p.getId()).orElseThrow()
        );
        try {
            String chartJson =
                chartService.getTotalCasesDeathsByCountryAndPandemic(
                    country,
                    pandemic
                );
            return ResponseEntity.ok(chartJson);
        } catch (ChartGeneratingException e) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                e.getMessage()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                "An internal server error occurred."
            );
        }
    }

    @Operation(
        summary = "Get chart data",
        description = "Bar chart representing total cases and deaths by country and pandemic."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Chart generated successfully",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "202",
                description = "Chart generation in progress",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            ),
        }
    )
    @GetMapping("/top10CountriesByCasesOrDeaths")
    public ResponseEntity<String> getTop10CountriesByCasesOrDeaths(
        @Schema(
            description = "The pandemic to filter by (either a JSON object with minimally {pandemic_id: number}, or a link to the pandemic)",
            anyOf = { Pandemic.class, String.class },
            required = false
        ) Optional<Pandemic> pandemic
    ) {
        pandemic = pandemic.map(p ->
            pandemicRepository.findById(p.getId()).orElseThrow()
        );
        try {
            String chartJson = chartService.getTop10CountriesByCasesOrDeaths(
                pandemic
            );
            return ResponseEntity.ok(chartJson);
        } catch (ChartGeneratingException e) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                e.getMessage()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                "An internal server error occurred."
            );
        }
    }

    @Operation(
        summary = "Get chart data",
        description = "Bar chart representing total cases and deaths by pandemic in the world."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Chart generated successfully",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "202",
                description = "Chart generation in progress",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            ),
        }
    )
    @GetMapping("/pandemicComparison")
    public ResponseEntity<String> getPandemicComparison() {
        try {
            String chartJson = chartService.getPandemicComparison();
            return ResponseEntity.ok(chartJson);
        } catch (ChartGeneratingException e) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                e.getMessage()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                "An internal server error occurred."
            );
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
            return ResponseEntity.ok(
                "{\"message\": \"All caches cleared successfully.\"}"
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                "{\"message\": \"Error occurred while clearing caches.\"}"
            );
        }
    }
}
