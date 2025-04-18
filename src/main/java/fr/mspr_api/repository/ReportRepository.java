package fr.mspr_api.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.mspr_api.component.Report;

@Repository
public interface ReportRepository extends CrudRepository<Report, Integer> {
    Report findByReportId(Integer reportId);
    List<Report> findByDate(Timestamp date);
    Report findByNewCases(Integer newCases);
    Report findByNewDeaths(Integer newDeaths);

    @Query("SELECT r FROM Report r WHERE r.infection.country.countryId = ?1")
    List<Report> findByCountryId(Integer countryId);

    @Query("SELECT r FROM Report r WHERE r.infection.pandemic.pandemicId = ?1")
    List<Report> findByPandemicId(Integer pandemicId);
}
