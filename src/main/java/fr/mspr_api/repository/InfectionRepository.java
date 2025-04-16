package fr.mspr_api.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.mspr_api.component.Country;
import fr.mspr_api.component.Infection;
import fr.mspr_api.component.Pandemic;

@Repository
public interface InfectionRepository extends CrudRepository<Infection, Integer> {
    Infection findByInfectionId(Integer infectionId);
    List<Infection> findByCountry(Country country);
    List<Infection> findByPandemic(Pandemic pandemic);
    List<Infection> findByTotalCases(Integer totalCases);
    List<Infection> findByTotalDeaths(Integer totalDeaths);
}
