package fr.mspr_api.service;

import fr.mspr_api.component.Country;
import fr.mspr_api.component.Infection;
import fr.mspr_api.component.Pandemic;
import fr.mspr_api.repository.InfectionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfectionService {

    private final InfectionRepository infectionRepository;

    private static final Logger logger = LoggerFactory.getLogger(InfectionService.class);

    public InfectionService(InfectionRepository infectionRepository) {
        this.infectionRepository = infectionRepository;
    }

    /**
     * Fetch infections by pandemic.
     * @param pandemic The Pandemic object.
     * @return List of infections associated with the given pandemic.
     * This method retrieves infections based on the pandemic.
     */
    public List<Infection> getInfectionsByPandemic(Pandemic pandemic) {
        return infectionRepository.findByPandemic(pandemic);
    }

    /**
     * Fetch infections by country.
     * @param country The Country object.
     * @return List of infections associated with the given country.
     * This method retrieves infections based on the country.
     */
    public List<Infection> getInfectionsByCountry(Country country) {
        return infectionRepository.findByCountry(country);
    }

    /**
     * Create a new infection.
     * @param infection The Infection object to create.
     * @return The created Infection object.
     * This method saves a new infection to the database.
     */
    public Infection createInfection(Infection infection) {
        return infectionRepository.save(infection);
    }

    /**
     * Update an existing infection.
     * @param id The ID of the infection to update.
     * @param updatedInfection The Infection object with updated data.
     * @throws IllegalArgumentException If the infection with the given ID does not exist.
     * @return The updated Infection object.
     * This method updates the fields of an existing infection based on the provided updatedInfection object.
     */
    public Infection updateInfection(Integer id, Infection updatedInfection) {
        return infectionRepository.findById(id)
            .map(infection -> {
                if (updatedInfection.getCountry() != null) {
                    logger.info("Updating country from {} to {}", infection.getCountry(), updatedInfection.getCountry());
                    infection.setCountry(updatedInfection.getCountry());
                }
                if (updatedInfection.getPandemic() != null) {
                    logger.info("Updating pandemic from {} to {}", infection.getPandemic(), updatedInfection.getPandemic());
                    infection.setPandemic(updatedInfection.getPandemic());
                }
                if (updatedInfection.getTotalCases() != null) {
                    logger.info("Updating total cases from {} to {}", infection.getTotalCases(), updatedInfection.getTotalCases());
                    infection.setTotalCases(updatedInfection.getTotalCases());
                }
                if (updatedInfection.getTotalDeaths() != null) {
                    logger.info("Updating total deaths from {} to {}", infection.getTotalDeaths(), updatedInfection.getTotalDeaths());
                    infection.setTotalDeaths(updatedInfection.getTotalDeaths());
                }
                return infectionRepository.save(infection);
            })
            .orElseThrow(() -> new IllegalArgumentException("Infection with ID " + id + " not found."));
    }

    /**
     * Delete an infection by ID.
     * @param id The ID of the infection to delete.
     * @throws IllegalArgumentException If the infection with the given ID does not exist.
     * @return void
     * This method deletes an infection from the database based on its ID.
     */
    public void deleteInfection(Integer id) {
        if (infectionRepository.existsById(id)) {
            infectionRepository.deleteById(id);
            System.out.println("Infection with ID " + id + " successfully deleted.");
        } else {
            throw new IllegalArgumentException("Infection with ID " + id + " not found.");
        }
    }
}
