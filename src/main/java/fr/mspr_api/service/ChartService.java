package fr.mspr_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.mspr_api.component.Country;
import fr.mspr_api.component.Infection;
import fr.mspr_api.component.Pandemic;
import fr.mspr_api.component.Report;
import fr.mspr_api.repository.CountryRepository;
import fr.mspr_api.repository.InfectionRepository;
import fr.mspr_api.repository.PandemicRepository;
import fr.mspr_api.repository.ReportRepository;
import jakarta.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChartService {

    public class ChartGeneratingException extends Exception {

        public ChartGeneratingException() {
            super("{\"message\":\"Chart is being generated. Please try again later.\"}");
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(
        ChartService.class
    );
    private static final String CACHE_DIR = "chart_cache";

    @Autowired
    private final InfectionRepository infectionRepository;

    @Autowired
    private final PandemicRepository pandemicRepository;

    @Autowired
    private final ReportRepository reportRepository;

    private final Set<String> generatingCharts = Collections.synchronizedSet(
        new HashSet<>()
    );

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

    public String getInfectionDistributionByContinent(Pandemic pandemic)
        throws IOException, ChartGeneratingException {
        final String cacheKey =
            "infectionDistributionByContinent_" + pandemic.getPandemicId();
        final String cacheFilePath = CACHE_DIR + "/" + cacheKey + ".json";

        File cacheFile = new File(cacheFilePath);
        if (cacheFile.exists()) {
            String json = Files.readString(cacheFile.toPath());
            return json;
        }

        if (generatingCharts.contains(cacheKey)) {
            throw new ChartGeneratingException();
        }

        generatingCharts.add(cacheKey);

        new Thread(() -> {
            try {
                Map<String, Long> continentData = StreamSupport.stream(
                    infectionRepository
                        .findAllByPandemic(pandemic)
                        .spliterator(),
                    false
                ).collect(
                    Collectors.groupingBy(
                        infection ->
                            infection.getCountry().getContinent().toString(),
                        Collectors.summingLong(Infection::getTotalCases)
                    )
                );

                Map<String, Object> chartData = new HashMap<>();
                chartData.put("type", "pie");
                chartData.put(
                    "data",
                    Map.of(
                        "labels",
                        new ArrayList<>(continentData.keySet()),
                        "datasets",
                        List.of(
                            Map.of(
                                "data",
                                new ArrayList<>(continentData.values()),
                                "backgroundColor",
                                List.of(
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

                String chartJson = new ObjectMapper()
                    .writeValueAsString(chartData);
                Files.writeString(Paths.get(cacheFilePath), chartJson);
            } catch (Exception e) {
                logger.error("Error generating chart", e);
            } finally {
                generatingCharts.remove(cacheKey);
            }
        }).start();

        throw new ChartGeneratingException();
    }

    public String getNewCasesDeathsOverTime(Country country, Pandemic pandemic)
        throws IOException, ChartGeneratingException {
        final String cacheKey =
            "newCasesDeathsOverTime_" +
            country.getCountryId() +
            "_" +
            pandemic.getPandemicId();
        final String cacheFilePath = CACHE_DIR + "/" + cacheKey + ".json";

        File cacheFile = new File(cacheFilePath);
        if (cacheFile.exists()) {
            String json = Files.readString(cacheFile.toPath());
            return json;
        }

        if (generatingCharts.contains(cacheKey)) {
            throw new ChartGeneratingException();
        }

        generatingCharts.add(cacheKey);

        Infection infection = infectionRepository.findByPandemicIdAndCountryId(
            pandemic.getPandemicId(),
            country.getCountryId()
        );

        new Thread(() -> {
            try {
                List<Report> reports = reportRepository.findAllByInfection(
                    infection
                );

                // Sort reports by date
                reports.sort(Comparator.comparing(Report::getDate));

                // Generate ChartJS data
                Map<String, Object> chartData = new HashMap<>();
                chartData.put("type", "line");

                // Prepare labels (dates)
                List<String> labels = reports
                    .stream()
                    .map(report -> report.getDate().toString())
                    .toList();

                // Prepare datasets
                List<Integer> newCases = reports
                    .stream()
                    .map(Report::getNewCases)
                    .toList();

                List<Integer> newDeaths = reports
                    .stream()
                    .map(Report::getNewDeaths)
                    .toList();

                Map<String, Object> newCasesDataset = new HashMap<>();
                newCasesDataset.put("label", "New Cases");
                newCasesDataset.put("borderColor", "rgba(75, 192, 192, 1)");
                newCasesDataset.put(
                    "backgroundColor",
                    "rgba(75, 192, 192, 0.2)"
                );
                newCasesDataset.put("data", newCases);

                Map<String, Object> newDeathsDataset = new HashMap<>();
                newDeathsDataset.put("label", "New Deaths");
                newDeathsDataset.put("borderColor", "rgba(255, 99, 132, 1)");
                newDeathsDataset.put(
                    "backgroundColor",
                    "rgba(255, 99, 132, 0.2)"
                );
                newDeathsDataset.put("data", newDeaths);

                Map<String, Object> data = new HashMap<>();
                data.put("labels", labels);
                data.put(
                    "datasets",
                    List.of(newCasesDataset, newDeathsDataset)
                );

                chartData.put("data", data);

                // Convert chart data to JSON
                String chartJson = new ObjectMapper()
                    .writeValueAsString(chartData);

                // Save to cache file
                Files.writeString(Paths.get(cacheFilePath), chartJson);
            } catch (Exception e) {
                generatingCharts.remove(cacheKey);
                logger.error("Error generating chart", e);
            }
        }).start();

        throw new ChartGeneratingException();
    }

    public String getTotalCasesDeathsByCountryAndPandemic(
        Optional<Country> country,
        Optional<Pandemic> pandemic
    ) throws IOException, ChartGeneratingException {
        final String cacheKey =
            "totalCasesDeathsByCountryAndPandemic_" +
            country
                .map(Country::getCountryId)
                .map(String::valueOf)
                .orElse("ALL") +
            "_" +
            pandemic
                .map(Pandemic::getPandemicId)
                .map(String::valueOf)
                .orElse("ALL");
        final String cacheFilePath = CACHE_DIR + "/" + cacheKey + ".json";

        File cacheFile = new File(cacheFilePath);
        if (cacheFile.exists()) {
            String json = Files.readString(cacheFile.toPath());
            return json;
        }

        if (generatingCharts.contains(cacheKey)) {
            throw new ChartGeneratingException();
        }

        generatingCharts.add(cacheKey);

        new Thread(() -> {
            try {
                Iterable<Infection> infections;

                if (country.isPresent() && pandemic.isPresent()) {
                    infections = List.of(
                        infectionRepository.findByPandemicIdAndCountryId(
                            pandemic.get().getPandemicId(),
                            country.get().getCountryId()
                        )
                    );
                } else if (country.isPresent()) {
                    infections = infectionRepository.findAllByCountry(
                        country.get()
                    );
                } else if (pandemic.isPresent()) {
                    infections = infectionRepository.findAllByPandemic(
                        pandemic.get()
                    );
                } else {
                    infections = infectionRepository.findAll();
                }

                Map<String, Object> chartData = new HashMap<>();
                chartData.put("type", "bar");

                Map<String, Object> labels = new HashMap<>();
                labels.put(
                    "labels",
                    StreamSupport.stream(infections.spliterator(), false)
                        .map(
                            i ->
                                i.getCountry().getName() +
                                " (" +
                                i.getPandemic().getName() +
                                ")"
                        )
                        .toList()
                );

                Map<String, Object> totalCases = new HashMap<>();
                totalCases.put("label", "Total Cases");
                totalCases.put(
                    "data",
                    StreamSupport.stream(infections.spliterator(), false)
                        .map(Infection::getTotalCases)
                        .toList()
                );

                Map<String, Object> totalDeaths = new HashMap<>();
                totalDeaths.put("label", "Total Deaths");
                totalDeaths.put(
                    "data",
                    StreamSupport.stream(infections.spliterator(), false)
                        .map(Infection::getTotalDeaths)
                        .toList()
                );

                Map<String, Object> totalCasesDataset = new HashMap<>();
                totalCasesDataset.put("label", "Total Cases");
                totalCasesDataset.put(
                    "backgroundColor",
                    "rgba(75, 192, 192, 0.6)"
                );
                totalCasesDataset.put("data", totalCases);

                Map<String, Object> totalDeathsDataset = new HashMap<>();
                totalDeathsDataset.put("label", "Total Deaths");
                totalDeathsDataset.put(
                    "backgroundColor",
                    "rgba(255, 99, 132, 0.6)"
                );
                totalDeathsDataset.put("data", totalDeaths);

                Map<String, Object> data = new HashMap<>();
                data.put("labels", labels);
                data.put(
                    "datasets",
                    List.of(totalCasesDataset, totalDeathsDataset)
                );

                chartData.put("data", data);

                String chartJson = new ObjectMapper()
                    .writeValueAsString(chartData);

                Files.writeString(Paths.get(cacheFilePath), chartJson);
            } catch (Exception e) {
                generatingCharts.remove(cacheKey);
                logger.error("Error generating chart", e);
            }
        }).start();

        throw new ChartGeneratingException();
    }

    public String getTop10CountriesByCasesOrDeaths(Optional<Pandemic> pandemic)
        throws IOException, ChartGeneratingException {
        final String cacheKey = "top10CountriesByCasesOrDeaths";
        final String cacheFilePath = CACHE_DIR + "/" + cacheKey + ".json";

        File cacheFile = new File(cacheFilePath);
        if (cacheFile.exists()) {
            String json = Files.readString(cacheFile.toPath());
            return json;
        }

        if (generatingCharts.contains(cacheKey)) {
            throw new ChartGeneratingException();
        }

        generatingCharts.add(cacheKey);

        new Thread(() -> {
            try {
                Iterable<Infection> infections = pandemic.isPresent()
                    ? infectionRepository.findAllByPandemic(pandemic.get())
                    : infectionRepository.findAll();
                infections = StreamSupport.stream(
                    infections.spliterator(),
                    false
                )
                    .sorted(
                        Comparator.comparingInt(
                            Infection::getTotalCases
                        ).reversed()
                    )
                    .limit(10)
                    .toList();

                Map<String, Object> chartData = new HashMap<>();
                chartData.put("type", "bar");
                chartData.put(
                    "data",
                    Map.of(
                        "labels",
                        StreamSupport.stream(infections.spliterator(), false)
                            .map(i -> i.getCountry().getName())
                            .toList(),
                        "datasets",
                        List.of(
                            Map.of(
                                "label",
                                "Total Cases",
                                "data",
                                StreamSupport.stream(
                                    infections.spliterator(),
                                    false
                                )
                                    .map(Infection::getTotalCases)
                                    .toList(),
                                "backgroundColor",
                                "rgba(75, 192, 192, 0.6)"
                            ),
                            Map.of(
                                "label",
                                "Total Deaths",
                                "data",
                                StreamSupport.stream(
                                    infections.spliterator(),
                                    false
                                )
                                    .map(Infection::getTotalDeaths)
                                    .toList(),
                                "backgroundColor",
                                "rgba(255, 99, 132, 0.6)"
                            )
                        )
                    )
                );

                String chartJson = new ObjectMapper()
                    .writeValueAsString(chartData);
                Files.writeString(Paths.get(cacheFilePath), chartJson);
            } catch (Exception e) {
                logger.error("Error generating chart", e);
            } finally {
                generatingCharts.remove(cacheKey);
            }
        }).start();

        throw new ChartGeneratingException();
    }

    public String getPandemicComparison()
        throws IOException, ChartGeneratingException {
        final String cacheKey = "pandemicComparison";
        final String cacheFilePath = CACHE_DIR + "/" + cacheKey + ".json";

        File cacheFile = new File(cacheFilePath);
        if (cacheFile.exists()) {
            return Files.readString(cacheFile.toPath());
        }

        if (generatingCharts.contains(cacheKey)) {
            throw new ChartGeneratingException();
        }

        generatingCharts.add(cacheKey);

        new Thread(() -> {
            try {
                Iterable<Pandemic> pandemics = pandemicRepository.findAll();

                Map<String, Object> chartData = new HashMap<>();
                chartData.put("type", "radar");
                chartData.put(
                    "data",
                    Map.of(
                        "labels",
                        List.of(
                            "Total Cases",
                            "Total Deaths",
                            "Duration (days)"
                        ),
                        "datasets",
                        StreamSupport.stream(pandemics.spliterator(), false)
                            .filter(
                                p ->
                                    p.getStartDate() != null &&
                                    p.getEndDate() != null
                            )
                            .map(p ->
                                Map.of(
                                    "label",
                                    p.getName(),
                                    "data",
                                    List.of(
                                        infectionRepository
                                            .findAllByPandemic(p)
                                            .stream()
                                            .mapToInt(Infection::getTotalCases)
                                            .sum(),
                                        infectionRepository
                                            .findAllByPandemic(p)
                                            .stream()
                                            .mapToInt(Infection::getTotalDeaths)
                                            .sum(),
                                        (int) ((p.getEndDate().getTime() -
                                                p.getStartDate().getTime()) /
                                            (1000 * 60 * 60 * 24))
                                    ),
                                    "backgroundColor",
                                    "rgba(75, 192, 192, 0.2)",
                                    "borderColor",
                                    "rgba(75, 192, 192, 1)"
                                )
                            )
                            .toList()
                    )
                );

                String chartJson = new ObjectMapper()
                    .writeValueAsString(chartData);
                Files.writeString(Paths.get(cacheFilePath), chartJson);
            } catch (Exception e) {
                logger.error("Error generating chart", e);
            } finally {
                generatingCharts.remove(cacheKey);
            }
        }).start();

        throw new ChartGeneratingException();
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
                            logger.warn(
                                "Failed to delete cache file: " + file.getName()
                            );
                        }
                    }
                }
            }
        }
    }
}
