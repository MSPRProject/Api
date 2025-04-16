package fr.mspr_api.service;

import fr.mspr_api.component.Country;
import fr.mspr_api.repository.CountryRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    /**
     * Create a new country.
     * @param country The Country object to create.
     * @return The created Country object.
     * This method saves a new country to the database.
     */
    public Country createCountry(Country country) {
        return countryRepository.save(country);
    }

    /**
     * Update an existing country.
     * @param id The ID of the country to update.
     * @param updatedCountry The Country object with updated data.
     * @throws IllegalArgumentException If the country with the given ID does not exist.
     * @return The updated Country object.
     * This method updates the fields of an existing country based on the provided updatedCountry object.
     */
    public Country updateCountry(Integer id, Country updatedCountry) {
        return countryRepository.findById(id)
        .map(country -> {
            if (updatedCountry.getName() != null) {
                logger.info("Updating name from {} to {}", country.getName(), updatedCountry.getName());
                country.setName(updatedCountry.getName());
            }
            if (updatedCountry.getIso3() != null) {
                logger.info("Updating ISO3 from '{}' to '{}'", country.getIso3(), updatedCountry.getIso3());
                country.setIso3(updatedCountry.getIso3());
            }
            if (updatedCountry.getContinent() != null) {
                logger.info("Updating continent from {} to {}", country.getContinent(), updatedCountry.getContinent());
                country.setContinent(updatedCountry.getContinent());
            }
            if (updatedCountry.getPopulation() != null) {
                logger.info("Updating population from {} to {}", country.getPopulation(), updatedCountry.getPopulation());
                country.setPopulation(updatedCountry.getPopulation());
            }
            return countryRepository.save(country);
        })
        .orElseThrow(() -> new IllegalArgumentException("Country with ID " + id + " not found."));
    }

    /**
     * Delete a country by its ID.
     * @param id The ID of the country to delete.
     * @throws IllegalArgumentException If the country with the given ID does not exist.
     * @return void
     * This method deletes a country from the database based on its ID.
     */
    public void deleteCountry(Integer id) {
        if (countryRepository.existsById(id)) {
            countryRepository.deleteById(id);
            System.out.println("Country with ID " + id + " successfully deleted.");
        } else {
            throw new IllegalArgumentException("Country with ID " + id + " not found.");
        }
    }
}
