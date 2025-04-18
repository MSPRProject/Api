package fr.mspr_api.service;

import fr.mspr_api.component.Pandemic;
import fr.mspr_api.repository.PandemicRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PandemicService {

    private final PandemicRepository pandemicRepository;

    private static final Logger logger = LoggerFactory.getLogger(PandemicService.class);

    public PandemicService(PandemicRepository pandemicRepository) {
        this.pandemicRepository = pandemicRepository;
    }

    /**
     * Get a list of all pandemics.
     */
    public Iterable<Pandemic> getAllPandemics() {
        return pandemicRepository.findAll();
    }

    /**
     * Create a new pandemic.
     * @param pandemic The Pandemic object to create.
     * @return The created Pandemic object.
     * This method saves a new pandemic to the database.
     */
    public Pandemic createPandemic(Pandemic pandemic) {
        return pandemicRepository.save(pandemic);
    }

    /**
     * Update an existing pandemic.
     * @param id The ID of the pandemic to update.
     * @param updatedPandemic The Pandemic object with updated data.
     * @throws IllegalArgumentException If the pandemic with the given ID does not exist.
     * @return The updated Pandemic object.
     * This method updates the fields of an existing pandemic based on the provided updatedPandemic object.
     */
    public Pandemic updatePandemic(Integer id, Pandemic updatedPandemic) {
        return pandemicRepository.findById(id)
            .map(pandemic -> {
                if (updatedPandemic.getName() != null) {
                    logger.info("Updating name from {} to {}", pandemic.getName(), updatedPandemic.getName());
                    pandemic.setName(updatedPandemic.getName());
                }
                if (updatedPandemic.getPathogen() != null) {
                    logger.info("Updating pathogen from {} to {}", pandemic.getPathogen(), updatedPandemic.getPathogen());
                    pandemic.setPathogen(updatedPandemic.getPathogen());
                }
                if (updatedPandemic.getDescription() != null) {
                    logger.info("Updating description from {} to {}", pandemic.getDescription(), updatedPandemic.getDescription());
                    pandemic.setDescription(updatedPandemic.getDescription());
                }
                if (updatedPandemic.getStartDate() != null) {
                    logger.info("Updating start date from {} to {}", pandemic.getStartDate(), updatedPandemic.getStartDate());
                    pandemic.setStartDate(updatedPandemic.getStartDate());
                }
                if (updatedPandemic.getEndDate() != null) {
                    logger.info("Updating end date from {} to {}", pandemic.getEndDate(), updatedPandemic.getEndDate());
                    pandemic.setEndDate(updatedPandemic.getEndDate());
                }
                if (updatedPandemic.getNotes() != null) {
                    logger.info("Updating notes from {} to {}", pandemic.getNotes(), updatedPandemic.getNotes());
                    pandemic.setNotes(updatedPandemic.getNotes());
                }
                return pandemicRepository.save(pandemic);
            })
            .orElseThrow(() -> new IllegalArgumentException("Pandemic with ID " + id + " not found."));
    }

    /**
     * Delete a pandemic by its ID.
     * @param id The ID of the pandemic to delete.
     * @throws IllegalArgumentException If the pandemic with the given ID does not exist.
     * @return void
     * This method deletes a pandemic from the database based on its ID.
     */
    public void deletePandemic(Integer id) {
        if (pandemicRepository.existsById(id)) {
            pandemicRepository.deleteById(id);
            System.out.println("Pandemic with ID " + id + " successfully deleted.");
        } else {
            throw new IllegalArgumentException("Pandemic with ID " + id + " not found.");
        }
    }
}
