package fr.mspr_api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.mspr_api.component.Continent;
import fr.mspr_api.component.Country;

@Repository
public interface CountryRepository extends CrudRepository<Country, Integer> {
    Country findByCountryId(Integer countryId);
    Country findByName(String name);
    Country findByIso3(String iso3);
    Country findByContinent(Continent continent);
    Country findByPopulation(Integer population);
}
