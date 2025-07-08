package fr.mspr_api.repository;

import fr.mspr_api.component.Continent;
import fr.mspr_api.component.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends CrudRepository<Country, Integer> {
    Page<Country> findAll(Pageable pageable);

    Country findByName(String name);
    Country findByIso3(String iso3);
    Page<Country> findAllByContinent(Continent continent, Pageable pageable);
}
