package fr.mspr_api.service;

import fr.mspr_api.component.Country;
import fr.mspr_api.component.Infection;
import fr.mspr_api.component.Pandemic;
import fr.mspr_api.component.Report;
import fr.mspr_api.repository.CountryRepository;
import fr.mspr_api.repository.InfectionRepository;
import fr.mspr_api.repository.PandemicRepository;
import fr.mspr_api.repository.ReportRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ExportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private InfectionRepository infectionRepository;

    @Autowired
    private PandemicRepository pandemicRepository;

    public Map<String, List<Object>> exportReportsAsJson() {
        List<Pandemic> pandemics = pandemicRepository
            .findAll(PageRequest.of(0, Integer.MAX_VALUE))
            .toList();
        List<Country> countries = countryRepository
            .findAll(PageRequest.of(0, Integer.MAX_VALUE))
            .toList();
        List<Infection> infections = infectionRepository
            .findAll(PageRequest.of(0, Integer.MAX_VALUE))
            .toList();
        List<Report> reports = reportRepository
            .findAll(PageRequest.of(0, Integer.MAX_VALUE))
            .toList();

        Map<String, List<Object>> data = new HashMap<>();
        data.put("pandemics", pandemics.stream().map(p -> (Object) p).toList());
        data.put("countries", countries.stream().map(c -> (Object) c).toList());
        data.put(
            "infections",
            infections.stream().map(i -> (Object) i).toList()
        );
        data.put("reports", reports.stream().map(r -> (Object) r).toList());

        return data;
    }

    public String exportReportsAsCsv() {
        Iterable<Report> reports = reportRepository.findAll(
            PageRequest.of(0, Integer.MAX_VALUE)
        );

        StringBuilder csv = new StringBuilder();
        csv.append(
            "id,continent,country_name,country_iso3,pandemic_name,pandemic_pathogen,pandemic_start_date,pandemic_end_date,pandemic_description,pandemic_notes,report_date,report_new_cases,report_new_deaths"
        );

        for (Report report : reports) {
            Infection infection = report.getInfection();
            Country country = infection.getCountry();
            Pandemic pandemic = infection.getPandemic();
            csv.append("\n");
            csv.append(report.getReportId());
            csv.append(",");
            csv.append(country.getContinent());
            csv.append(",");
            csv.append(country.getName());
            csv.append(",");
            csv.append(country.getIso3());
            csv.append(",");
            csv.append(pandemic.getName());
            csv.append(",");
            csv.append(
                pandemic.getPathogen() != null ? pandemic.getPathogen() : ""
            );
            csv.append(",");
            csv.append(
                pandemic.getStartDate() != null
                    ? pandemic.getStartDate().toString()
                    : ""
            );
            csv.append(",");
            csv.append(
                pandemic.getEndDate() != null
                    ? pandemic.getEndDate().toString()
                    : ""
            );
            csv.append(",");
            csv.append(
                pandemic.getDescription() != null
                    ? pandemic.getDescription()
                    : ""
            );
            csv.append(",");
            csv.append(pandemic.getNotes() != null ? pandemic.getNotes() : "");
            csv.append(",");
            csv.append(report.getDate());
            csv.append(",");
            csv.append(report.getNewCases());
            csv.append(",");
            csv.append(report.getNewDeaths());
        }

        return csv.toString();
    }
}
