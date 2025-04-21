package fr.mspr_api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.mspr_api.component.Continent;
import fr.mspr_api.component.Country;

import java.util.List;

@Repository
public interface CountryRepository extends CrudRepository<Country, Integer> {
    Country findByName(String name);
    Country findByIso3(String iso3);
    List<Country> findAllByContinent(Continent continent);
}
