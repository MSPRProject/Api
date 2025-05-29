package fr.mspr_api.service;

import fr.mspr_api.component.Infection;
import fr.mspr_api.component.Report;
import fr.mspr_api.config.AiApiConfig;
import fr.mspr_api.repository.ReportRepository;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AiService {

    @Autowired
    private final AiApiConfig aiApiConfig;

    @Autowired
    private final ReportRepository reportRepository;

    public AiService(
        AiApiConfig aiApiConfig,
        ReportRepository reportRepository
    ) {
        this.aiApiConfig = aiApiConfig;
        this.reportRepository = reportRepository;
    }

    @SuppressWarnings("unchecked")
    public Report predict(Date predictAt, Infection infection) {
        List<Report> reports =
            reportRepository.find100LatestByInfectionBeforeDate(
                infection,
                predictAt
            );

        List<Map<String, Object>> serializedReports = reports
            .stream()
            .map(r -> {
                Map<String, Object> res = new HashMap<>();
                res.put("new_cases", r.getNewCases());
                res.put("new_deaths", r.getNewDeaths());
                res.put("date", r.getDate());
                return res;
            })
            .toList();

        Map<String, Object> serializedPrompt = new HashMap<>();
        serializedPrompt.put("reports", serializedReports);
        serializedPrompt.put(
            "pandemic_name",
            infection.getPandemic().getName()
        );
        serializedPrompt.put(
            "pandemic_pathogen",
            infection.getPandemic().getPathogen()
        );
        serializedPrompt.put("country_iso3", infection.getCountry().getIso3());
        serializedPrompt.put(
            "continent",
            infection.getCountry().getContinent()
        );

        Map<String, Object> serializedTarget = new HashMap<>();
        serializedTarget.put("date", predictAt);
        serializedPrompt.put("target", serializedTarget);

        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + aiApiConfig.getApiKey());

        Map<String, Object> response = client.postForObject(
            aiApiConfig.getPromptUrl(),
            serializedPrompt,
            Map.class
        );

        Report report = new Report(
            infection,
            predictAt,
            ((Double) response.get("new_cases")).intValue(),
            ((Double) response.get("new_deaths")).intValue()
        );

        this.reportRepository.save(report);

        return report;
    }

    public Map<String, Object> getTrainingData(Report report) {
        List<Report> previousReports =
            reportRepository.find100LatestByInfectionBeforeDate(
                report.getInfection(),
                report.getDate()
            );

        Map<String, Object> serializedTrainingData = new HashMap<>();
        serializedTrainingData.put(
            "reports",
            previousReports
                .stream()
                .map(r -> {
                    Map<String, Object> res = new HashMap<>();
                    res.put("new_cases", r.getNewCases());
                    res.put("new_deaths", r.getNewDeaths());
                    res.put("date", r.getDate());
                    return res;
                })
                .toList()
        );

        serializedTrainingData.put(
            "pandemic_name",
            report.getInfection().getPandemic().getName()
        );
        serializedTrainingData.put(
            "pandemic_pathogen",
            report.getInfection().getPandemic().getPathogen()
        );
        serializedTrainingData.put(
            "country_iso3",
            report.getInfection().getCountry().getIso3()
        );
        serializedTrainingData.put(
            "continent",
            report.getInfection().getCountry().getContinent()
        );

        Map<String, Object> serializedOutput = new HashMap<>();
        serializedOutput.put("new_cases", report.getNewCases());
        serializedOutput.put("new_deaths", report.getNewDeaths());
        serializedOutput.put("date", report.getDate());

        serializedTrainingData.put("target", serializedOutput);

        return serializedTrainingData;
    }
}
