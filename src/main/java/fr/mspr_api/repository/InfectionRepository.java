package fr.mspr_api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.mspr_api.component.Country;
import fr.mspr_api.component.Infection;
import fr.mspr_api.component.Pandemic;

@Repository
public interface InfectionRepository extends CrudRepository<Infection, Integer> {
    Infection findByInfectionId(Integer infectionId);
    Infection findByCountry(Country country);
    Infection findByPandemic(Pandemic pandemic);
    Infection findByTotalCases(Integer totalCases);
    Infection findByTotalDeaths(Integer totalDeaths);
}
