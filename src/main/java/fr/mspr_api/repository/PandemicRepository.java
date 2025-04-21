package fr.mspr_api.repository;

import fr.mspr_api.component.Pandemic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PandemicRepository extends CrudRepository<Pandemic, Integer> {
    Page<Pandemic> findAll(Pageable pageable);

    Pandemic findByName(String name);
    Page<Pandemic> findAllByPathogen(String pathogen, Pageable pageable);
}
