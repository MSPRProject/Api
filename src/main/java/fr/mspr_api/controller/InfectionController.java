package fr.mspr_api.controller;

import fr.mspr_api.component.Country;
import fr.mspr_api.component.Infection;
import fr.mspr_api.component.Pandemic;
import fr.mspr_api.repository.CountryRepository;
import fr.mspr_api.repository.PandemicRepository;
import fr.mspr_api.service.InfectionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/infections")
public class InfectionController {

    private final InfectionService infectionService;
    private final CountryRepository countryRepository;
    private final PandemicRepository pandemicRepository;

    public InfectionController(InfectionService infectionService, CountryRepository countryRepository, PandemicRepository pandemicRepository) {
        this.infectionService = infectionService;
        this.countryRepository = countryRepository;
        this.pandemicRepository = pandemicRepository;
    }

    /**
     * Get all infections by pandemic.
     * @param pandemic The Pandemic object .
     * @return List of infections associated with the given pandemic.
     * This method retrieves infections based on the pandemic.
     */
    @Operation(summary = "Get infections by pandemic", description = "Retrieve all infections associated with a specific pandemic ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Infections retrieved successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Infection.class))),
        @ApiResponse(responseCode = "404", description = "Pandemic not found")
    })
    @GetMapping("/pandemics/{id_pandemic}")
    public List<Infection> getInfectionsByPandemic(@PathVariable("id_pandemic") @Schema(description = "Pandemic object") Pandemic pandemic) {
        return infectionService.getInfectionsByPandemic(pandemic);
    }

    /**
     * Get all infections by country.
     * @param country The Country object.
     * @return List of infections associated with the given country.
     * This method retrieves infections based on the country.
     */
    @Operation(summary = "Get infections by country", description = "Retrieve all infections associated with a specific country ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Infections retrieved successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Infection.class))),
        @ApiResponse(responseCode = "404", description = "Country not found")
    })
    @GetMapping("/countries/{id_country}")
    public List<Infection> getInfectionsByCountry(@PathVariable("id_country") @Schema(description = "Pandemic object") Country country) {
        return infectionService.getInfectionsByCountry(country);
    }
    
    /**
     * Get all infections by date.
     * @param infection The Infection object.
     * @return List of infections associated with the given date.
     * This method retrieves infections based on the date.
     */
    @Operation(summary = "Create a new infection", description = "Create a new infection in the database.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Infection created successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Infection.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public Infection createInfection(@RequestBody @Schema(description = "Infection object") Infection infection) {
        System.out.println("Received infection: " + infection);

        if (infection.getCountry() == null || infection.getCountry().getCountryId() == null) {
            throw new IllegalArgumentException("Country ID is required");
        }
        if (infection.getPandemic() == null || infection.getPandemic().getPandemicId() == null) {
            throw new IllegalArgumentException("Pandemic ID is required");
        }
    
        Country country = countryRepository.findById(infection.getCountry().getCountryId())
            .orElseThrow(() -> new IllegalArgumentException("Country not found"));
        Pandemic pandemic = pandemicRepository.findById(infection.getPandemic().getPandemicId())
            .orElseThrow(() -> new IllegalArgumentException("Pandemic not found"));
    
        infection.setCountry(country);
        infection.setPandemic(pandemic);
        infection.setTotalCases(infection.getTotalCases() != null ? infection.getTotalCases() : 0);
        infection.setTotalDeaths(infection.getTotalDeaths() != null ? infection.getTotalDeaths() : 0);
        
        return infectionService.createInfection(infection);
    }

    /**
     * Update an existing infection.
     * @param id The ID of the infection to update.
     * @param updatedInfection The Infection object with updated data.
     * @return The updated Infection object.
     * This method updates the fields of an existing infection based on the provided updatedInfection object.
     */
    @Operation(summary = "Update an existing infection", description = "Update the fields of an existing infection.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Infection updated successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Infection.class))),
        @ApiResponse(responseCode = "404", description = "Infection not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PatchMapping("/{id}")
    public Infection updateInfection(@PathVariable("id") @Schema(description = "Infection ID") Integer id, 
                                     @RequestBody @Schema(description = "Updated infection object") Infection updatedInfection) {
        return infectionService.updateInfection(id, updatedInfection);
    }
    
    /**
     * Delete an infection by ID.
     * @param id The ID of the infection to delete.
     * @return void
     * This method deletes an infection from the database based on the provided ID.
     */
    @Operation(summary = "Delete an infection by ID", description = "Delete an infection from the database based on its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Infection deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Infection not found")
    })
    @DeleteMapping("/{id}")
    public void deleteInfection(@PathVariable("id") @Schema(description = "Infection ID") Integer id) {
        infectionService.deleteInfection(id);
    }
}
