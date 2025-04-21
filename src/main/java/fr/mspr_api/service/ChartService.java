package fr.mspr_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.mspr_api.component.Infection;
import fr.mspr_api.component.Pandemic;
import fr.mspr_api.component.Report;
import fr.mspr_api.repository.InfectionRepository;
import fr.mspr_api.repository.PandemicRepository;
import fr.mspr_api.repository.ReportRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ChartService {

    private static final Logger logger = LoggerFactory.getLogger(ChartService.class);
    private static final String CACHE_DIR = "chart_cache";

    private final InfectionRepository infectionRepository;
    private final PandemicRepository pandemicRepository;
    private final ReportRepository reportRepository;

    public ChartService(
        InfectionRepository infectionRepository,
        PandemicRepository pandemicRepository,
        ReportRepository reportRepository
    ) {
        this.infectionRepository = infectionRepository;
        this.pandemicRepository = pandemicRepository;
        this.reportRepository = reportRepository;

        // Ensure cache directory exists
        File cacheDir = new File(CACHE_DIR);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }

    public String getInfectionDistributionByContinent(Integer pandemicId) throws IOException {
        Pandemic pandemic = pandemicRepository.findById(pandemicId)
            .orElseThrow(() -> new EntityNotFoundException("Pandemic not found"));

        Map<String, Long> continentData = StreamSupport.stream(
            infectionRepository.findAllByPandemic(pandemic).spliterator(),
            false
        ).collect(
            Collectors.groupingBy(
                infection -> infection.getCountry().getContinent().toString(),
                Collectors.summingLong(Infection::getTotalCases)
            )
        );

        Map<String, Object> chartData = new HashMap<>();
        chartData.put("type", "pie");
        chartData.put(
            "data",
            Map.of(
                "labels", new ArrayList<>(continentData.keySet()),
                "datasets", List.of(
                    Map.of(
                        "data", new ArrayList<>(continentData.values()),
                        "backgroundColor", List.of(
                            "rgba(255, 99, 132, 0.6)",
                            "rgba(54, 162, 235, 0.6)",
                            "rgba(255, 206, 86, 0.6)",
                            "rgba(75, 192, 192, 0.6)",
                            "rgba(153, 102, 255, 0.6)",
                            "rgba(255, 159, 64, 0.6)",
                            "rgba(201, 203, 207, 0.6)"
                        )
                    )
                )
            )
        );

        return new ObjectMapper().writeValueAsString(chartData);
    }

    public String getNewCasesDeathsOverTime(Integer countryId, Integer pandemicId) throws IOException {
        Infection infection = infectionRepository.findByPandemicIdAndCountryId(pandemicId, countryId);

        List<Report> reports = reportRepository.findAllByInfection(infection);
        reports.sort(Comparator.comparing(Report::getDate));

        Map<String, Object> chartData = new HashMap<>();
        chartData.put("type", "line");

        List<String> labels = reports.stream()
            .map(report -> report.getDate().toString())
            .toList();

        List<Integer> newCases = reports.stream()
            .map(Report::getNewCases)
            .toList();

        List<Integer> newDeaths = reports.stream()
            .map(Report::getNewDeaths)
            .toList();

        Map<String, Object> newCasesDataset = new HashMap<>();
        newCasesDataset.put("label", "New Cases");
        newCasesDataset.put("borderColor", "rgba(75, 192, 192, 1)");
        newCasesDataset.put("backgroundColor", "rgba(75, 192, 192, 0.2)");
        newCasesDataset.put("data", newCases);

        Map<String, Object> newDeathsDataset = new HashMap<>();
        newDeathsDataset.put("label", "New Deaths");
        newDeathsDataset.put("borderColor", "rgba(255, 99, 132, 1)");
        newDeathsDataset.put("backgroundColor", "rgba(255, 99, 132, 0.2)");
        newDeathsDataset.put("data", newDeaths);

        chartData.put("data", Map.of(
            "labels", labels,
            "datasets", List.of(newCasesDataset, newDeathsDataset)
        ));

        return new ObjectMapper().writeValueAsString(chartData);
    }

    public void pruneCaches() {
        File cacheDir = new File(CACHE_DIR);
        if (cacheDir.exists() && cacheDir.isDirectory()) {
            File[] files = cacheDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        boolean deleted = file.delete();
                        if (!deleted) {
                            logger.warn("Failed to delete cache file: " + file.getName());
                        }
                    }
                }
            }
        }
    }
}