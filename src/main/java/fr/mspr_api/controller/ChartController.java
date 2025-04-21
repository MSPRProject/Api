package fr.mspr_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.mspr_api.component.Country;
import fr.mspr_api.component.Infection;
import fr.mspr_api.component.Pandemic;
import fr.mspr_api.component.Report;
import fr.mspr_api.repository.CountryRepository;
import fr.mspr_api.repository.InfectionRepository;
import fr.mspr_api.repository.PandemicRepository;
import fr.mspr_api.repository.ReportRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/charts")
public class ChartController {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private PandemicRepository pandemicRepository;

    @Autowired
    private InfectionRepository infectionRepository;

    @Autowired
    private ReportRepository reportRepository;

    private static final Logger logger = LoggerFactory.getLogger(
        ChartController.class
    );

    private static final String CACHE_DIR = "chart_cache";

    private final Set<String> generatingCharts = Collections.synchronizedSet(
        new HashSet<>()
    );

    public ChartController() {
        File cacheDir = new File(CACHE_DIR);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }

    @GetMapping("/infectionDistributionByContinent")
    public ResponseEntity<?> getInfectionDistributionByContinent(
        Integer pandemicId
    ) {
        final String cacheKey =
            "infectionDistributionByContinent_" + pandemicId;
        final String cacheFilePath = CACHE_DIR + "/" + cacheKey + ".json";

        File cacheFile = new File(cacheFilePath);
        if (cacheFile.exists()) {
            try {
                String json = Files.readString(cacheFile.toPath());
                return ResponseEntity.ok(json);
            } catch (IOException e) {
                return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
                ).body("Error reading cache file");
            }
        }

        if (generatingCharts.contains(cacheKey)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                "Chart is being generated. Please try again later."
            );
        }

        generatingCharts.add(cacheKey);

        Pandemic pandemic = pandemicRepository
            .findById(pandemicId)
            .orElseThrow(() -> new EntityNotFoundException("Pandemic not found")
            );

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

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
            "{\"message\": \"Chart is being generated. Please try again later.\"}"
        );
    }

    @Operation(
        summary = "Generate a chart",
        description = "Total case and Death number by country and pandemic"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "202",
                description = "Chart is generating, please wait"
            ),
            @ApiResponse(
                responseCode = "200",
                description = "Chart generated successfully",
                content = @Content(mediaType = "application/json")
            ),
        }
    )
    @GetMapping("/totalCasesDeathsByCountryAndPandemic")
    public ResponseEntity<?> getTotalCasesDeathsByCountryAndPandemic(
        Optional<Integer> countryId,
        Optional<Integer> pandemicId
    ) {
        final String cacheKey =
            "totalCasesDeathsByCountryAndPandemic_" +
            countryId +
            "_" +
            pandemicId;
        final String cacheFilePath = CACHE_DIR + "/" + cacheKey + ".json";

        File cacheFile = new File(cacheFilePath);
        if (cacheFile.exists()) {
            try {
                String json = Files.readString(cacheFile.toPath());
                return ResponseEntity.ok(json);
            } catch (IOException e) {
                return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
                ).body("Error reading cache file");
            }
        }

        if (generatingCharts.contains(cacheKey)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                "Chart is being generated. Please try again later."
            );
        }

        generatingCharts.add(cacheKey);

        new Thread(() -> {
            try {
                Iterable<Infection> infections;
                Optional<Country> country;
                Optional<Pandemic> pandemic;

                if (countryId.isPresent() && pandemicId.isPresent()) {
                    infections = List.of(
                        infectionRepository.findByPandemicIdAndCountryId(
                            pandemicId.get(),
                            countryId.get()
                        )
                    );
                } else if (countryId.isPresent()) {
                    country = countryRepository.findById(countryId.get());
                    if (!country.isPresent()) {
                        generatingCharts.remove(cacheKey);
                        throw new IllegalArgumentException("Country not found");
                    }

                    infections = infectionRepository.findAllByCountry(
                        country.get()
                    );
                } else if (pandemicId.isPresent()) {
                    pandemic = pandemicRepository.findById(pandemicId.get());
                    if (!pandemic.isPresent()) {
                        generatingCharts.remove(cacheKey);
                        throw new IllegalArgumentException(
                            "Pandemic not found"
                        );
                    }

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

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
            "{\"message\": \"Chart is being generated. Please try again later.\"}"
        );
    }

    @Operation(
        summary = "Generate a chart",
        description = "New cases and deaths over time for a specific country or pandemic"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "202",
                description = "Chart is generating, please wait"
            ),
            @ApiResponse(
                responseCode = "200",
                description = "Chart generated successfully",
                content = @Content(mediaType = "application/json")
            ),
        }
    )
    @GetMapping("/newCasesDeathsOverTime")
    public ResponseEntity<?> getNewCasesDeathsOverTime(
        Integer countryId,
        Integer pandemicId
    ) {
        final String cacheKey =
            "newCasesDeathsOverTime_" + countryId + "_" + pandemicId;
        final String cacheFilePath = CACHE_DIR + "/" + cacheKey + ".json";

        File cacheFile = new File(cacheFilePath);
        if (cacheFile.exists()) {
            try {
                String json = Files.readString(cacheFile.toPath());
                return ResponseEntity.ok(json);
            } catch (IOException e) {
                return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
                ).body("Error reading cache file");
            }
        }

        if (generatingCharts.contains(cacheKey)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                "Chart is being generated. Please try again later."
            );
        }

        generatingCharts.add(cacheKey);

        Infection infection = infectionRepository.findByPandemicIdAndCountryId(
            pandemicId,
            countryId
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

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
            "{\"message\": \"Chart is being generated. Please try again later.\"}"
        );
    }

    @GetMapping("/top10CountriesByCasesOrDeaths")
    public ResponseEntity<?> getTop10CountriesByCasesOrDeaths() {
        final String cacheKey = "top10CountriesByCasesOrDeaths";
        final String cacheFilePath = CACHE_DIR + "/" + cacheKey + ".json";

        File cacheFile = new File(cacheFilePath);
        if (cacheFile.exists()) {
            try {
                String json = Files.readString(cacheFile.toPath());
                return ResponseEntity.ok(json);
            } catch (IOException e) {
                return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
                ).body("Error reading cache file");
            }
        }

        if (generatingCharts.contains(cacheKey)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                "Chart is being generated. Please try again later."
            );
        }

        generatingCharts.add(cacheKey);

        new Thread(() -> {
            try {
                List<Infection> infections = StreamSupport.stream(
                    infectionRepository.findAll().spliterator(),
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
                        infections
                            .stream()
                            .map(i -> i.getCountry().getName())
                            .toList(),
                        "datasets",
                        List.of(
                            Map.of(
                                "label",
                                "Total Cases",
                                "data",
                                infections
                                    .stream()
                                    .map(Infection::getTotalCases)
                                    .toList(),
                                "backgroundColor",
                                "rgba(75, 192, 192, 0.6)"
                            ),
                            Map.of(
                                "label",
                                "Total Deaths",
                                "data",
                                infections
                                    .stream()
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

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
            "{\"message\": \"Chart is being generated. Please try again later.\"}"
        );
    }

    /**
     * Prune (clear) all cached charts.
     * This endpoint deletes all cached files and clears the generatingCharts set.
     *
     * @return ResponseEntity with the result of the operation.
     */
    @Operation(
        summary = "Prune all cached charts",
        description = "Deletes all cached chart files and clears the cache."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "All caches cleared successfully",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error occurred while clearing caches",
                content = @Content(mediaType = "application/json")
            ),
        }
    )
    @GetMapping("/pruneCaches")
    public ResponseEntity<?> pruneCaches() {
        try {
            // Clear all files in the cache directory
            File cacheDir = new File(CACHE_DIR);
            if (cacheDir.exists() && cacheDir.isDirectory()) {
                File[] files = cacheDir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile()) {
                            boolean deleted = file.delete();
                            if (!deleted) {
                                logger.warn(
                                    "Failed to delete cache file: " +
                                    file.getName()
                                );
                            }
                        }
                    }
                }
            }

            // Clear the generatingCharts set
            generatingCharts.clear();

            logger.info("All caches cleared successfully.");
            return ResponseEntity.ok(
                "{\"message\": \"All caches cleared successfully.\"}"
            );
        } catch (Exception e) {
            logger.error("Error occurred while clearing caches", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                "{\"message\": \"Error occurred while clearing caches.\"}"
            );
        }
    }
}
