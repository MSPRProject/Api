package fr.mspr_api.repository;

import fr.mspr_api.component.Report;
import java.sql.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends CrudRepository<Report, Integer> {
    Report findByReportId(Integer reportId);
    List<Report> findByDate(Date date);

    @Query(
        "SELECT r FROM Report r WHERE r.infection.country.countryId = :country_id"
    )
    List<Report> findByCountryId(@Param("country_id") Integer country_id);

    @Query(
        "SELECT r FROM Report r WHERE r.infection.pandemic.pandemicId = :pandemic_id"
    )
    List<Report> findByPandemicId(@Param("pandemic_id") Integer pandemicId);

    @Query(
        "SELECT r FROM Report r WHERE r.date = :date AND r.infection.pandemic.pandemicId = :pandemic_id AND r.infection.country.countryId = :country_id"
    )
    Report findByDateAndCountryIdAndPandemicId(
        Date date,
        @Param("country_id") Integer countryId,
        @Param("pandemic_id") Integer pandemicId
    );
}
