package fr.mspr_api.repository;

import fr.mspr_api.component.Pandemic;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PandemicRepository extends CrudRepository<Pandemic, Integer> {
    Pandemic findByName(String name);
    List<Pandemic> findAllByPathogen(String pathogen);
}
