package fr.mspr_api.controller;

import fr.mspr_api.component.Pandemic;
import fr.mspr_api.service.PandemicService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pandemics")
public class PandemicController {

    private final PandemicService pandemicService;

    public PandemicController(PandemicService pandemicService) {
        this.pandemicService = pandemicService;
    }

    /**
     * Create a new pandemic.
     * @param pandemic The Pandemic object to create.
     * @return The created Pandemic object.
     * This method saves a new pandemic to the database.
     */
    @Operation(summary = "Create a new pandemic", description = "Create a new pandemic in the database.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pandemic created successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pandemic.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public Pandemic createPandemic(@RequestBody @Schema(description = "Pandemic object") Pandemic pandemic) {
        return pandemicService.createPandemic(pandemic);
    }

    /**
     * Update an existing pandemic.
     * @param id The ID of the pandemic to update.
     * @param updatedPandemic The Pandemic object with updated data.
     * @return The updated Pandemic object.
     * This method updates the fields of an existing pandemic based on the provided updatedPandemic object.
     */
    @Operation(summary = "Update an existing pandemic", description = "Update the fields of an existing pandemic.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pandemic updated successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pandemic.class))),
        @ApiResponse(responseCode = "404", description = "Pandemic not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PatchMapping("/{id}")
    public Pandemic updatePandemic(@PathVariable("id") @Schema(description = "Pandemic ID") Integer id, 
                                   @RequestBody @Schema(description = "Pandemic object") Pandemic updatedPandemic) {
        return pandemicService.updatePandemic(id, updatedPandemic);
    }

    /**
     * Delete a pandemic by ID.
     * @param id The ID of the pandemic to delete.
     * @return void
     * This method deletes a pandemic from the database based on the provided ID.
     */
    @Operation(summary = "Delete a pandemic by ID", description = "Delete a pandemic from the database based on its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pandemic deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Pandemic not found")
    })
    @DeleteMapping("/{id}")
    public void deletePandemic(@PathVariable("id") @Schema(description = "Pandemic ID") Integer id) {
        pandemicService.deletePandemic(id);
    }
}
