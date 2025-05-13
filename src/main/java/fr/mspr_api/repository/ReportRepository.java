package fr.mspr_api.repository;

import fr.mspr_api.component.Infection;
import fr.mspr_api.component.Report;
import java.sql.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends CrudRepository<Report, Integer> {
    Page<Report> findAll(Pageable pageable);

    Report findByReportId(Integer reportId);
    Page<Report> findByDate(Date date, Pageable pageable);
    Page<Report> findAllByInfection(Infection infection, Pageable pageable);

    @Query(
        "SELECT r FROM Report r WHERE r.infection.country.countryId = :country_id"
    )
    Page<Report> findAllByCountryId(
        @Param("country_id") Integer country_id,
        Pageable pageable
    );

    @Query(
        "SELECT r FROM Report r WHERE r.infection.pandemic.pandemicId = :pandemic_id"
    )
    Page<Report> findAllByPandemicId(
        @Param("pandemic_id") Integer pandemicId,
        Pageable pageable
    );

    @Query(
        "SELECT r FROM Report r WHERE r.date = :date AND r.infection.pandemic.pandemicId = :pandemic_id AND r.infection.country.countryId = :country_id"
    )
    Report findByDateAndCountryIdAndPandemicId(
        Date date,
        @Param("country_id") Integer countryId,
        @Param("pandemic_id") Integer pandemicId
    );

    @Query(
        "SELECT r FROM Report r WHERE r.infection = :infection AND r.date <= :date ORDER BY r.date DESC LIMIT 100"
    )
    List<Report> find100LatestByInfectionBeforeDate(
        @Param("infection") Infection infection,
        @Param("date") Date date
    );
}
