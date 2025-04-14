package fr.mspr_api.repository;

import java.security.Timestamp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.mspr_api.component.Pandemic;

@Repository
public interface PandemicRepository extends CrudRepository<Pandemic, Integer> {
    Pandemic findByPandemicId(Integer pandemicId);
    Pandemic findByName(String name);
    Pandemic findByPathogen(String pathogen);
    Pandemic findByDescription(String description);
    Pandemic findByStartDate(Timestamp startDate);
    Pandemic findByEndDate(Timestamp endDate);
    Pandemic findByNotes(String notes);
}
