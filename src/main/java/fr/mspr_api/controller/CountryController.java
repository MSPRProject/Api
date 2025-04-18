package fr.mspr_api.controller;

import fr.mspr_api.component.Country;
import fr.mspr_api.service.CountryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    /**
     * Create a new country.
     * @param country The Country object to create.
     * @return The created Country object.
     * This method saves a new country to the database.
     */
    @Operation(summary = "Create a new country", description = "Create a new country in the database.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Country created successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Country.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public Country createCountry(@RequestBody @Schema(description = "Pandemic object") Country country) {
        return countryService.createCountry(country);
    }

    /**
     * Update an existing country.
     * @param id The ID of the country to update.
     * @param updatedCountry The Country object with updated data.
     * @return The updated Country object.
     * This method updates the fields of an existing country based on the provided updatedCountry object.
     */
    @Operation(summary = "Update an existing country", description = "Update the fields of an existing country.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Country updated successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Country.class))),
        @ApiResponse(responseCode = "404", description = "Country not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PatchMapping("/{id}")
    public Country updateCountry(@PathVariable("id") @Schema(description = "Pandemic object") Integer id, 
                                 @RequestBody @Schema(description = "Pandemic object") Country updatedCountry) {
        return countryService.updateCountry(id, updatedCountry);
    }

    /**
     * Delete a country by ID.
     * @param id The ID of the country to delete.
     * @return void
     * This method deletes a country from the database based on the provided ID.
     */
    @Operation(summary = "Delete a country by ID", description = "Delete a country from the database based on its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Country deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Country not found")
    })
    @DeleteMapping("/{id}")
    public void deleteCountry(@PathVariable("id") @Schema(description = "Country ID") Integer id) {
        countryService.deleteCountry(id);
    }
}
