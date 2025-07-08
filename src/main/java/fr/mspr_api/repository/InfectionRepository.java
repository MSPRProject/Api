package fr.mspr_api.repository;

import fr.mspr_api.component.Country;
import fr.mspr_api.component.Infection;
import fr.mspr_api.component.Pandemic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InfectionRepository
    extends CrudRepository<Infection, Integer> {
    Page<Infection> findAll(Pageable pageable);

    Page<Infection> findAllByCountry(Country country, Pageable pageable);
    Page<Infection> findAllByPandemic(Pandemic pandemic, Pageable pageable);

    @Query(
        "SELECT i FROM Infection i WHERE i.pandemic.pandemicId = :pandemic_id AND i.country.countryId = :country_id"
    )
    Infection findByPandemicIdAndCountryId(
        @Param("pandemic_id") Integer pandemic_id,
        @Param("country_id") Integer country_id
    );
}
