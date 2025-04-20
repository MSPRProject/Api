package fr.mspr_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.mspr_api.component.Country;
import fr.mspr_api.component.Infection;
import fr.mspr_api.component.Pandemic;

@Repository
public interface InfectionRepository extends CrudRepository<Infection, Integer> {
    List<Infection> findAllByCountry(Country country);
    List<Infection> findAllByPandemic(Pandemic pandemic);

    @Query("SELECT i FROM Infection i WHERE i.pandemic.pandemicId = :pandemic_id AND i.country.countryId = :country_id")
    Infection findByPandemicAndCountry(@Param("pandemic_id") Integer pandemic_id, @Param("country_id") Integer country_id);
}
