package fr.mspr_api.controller;

import fr.mspr_api.component.Report;
import fr.mspr_api.repository.InfectionRepository;
import fr.mspr_api.repository.ReportRepository;
import fr.mspr_api.service.AiService;
import java.sql.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
class AiController {

    @Autowired
    private final AiService aiService;

    @Autowired
    private final ReportRepository reportRepository;

    @Autowired
    private final InfectionRepository infectionRepository;

    public AiController(
        AiService aiService,
        ReportRepository reportRepository,
        InfectionRepository infectionRepository
    ) {
        this.aiService = aiService;
        this.reportRepository = reportRepository;
        this.infectionRepository = infectionRepository;
    }

    @GetMapping("/trainingData")
    public Page<Map<String, Object>> getTrainingData(Pageable pageable) {
        Page<Report> reports = reportRepository.findAll(pageable);

        return new PageImpl<>(
            reports
                .stream()
                .map(r -> this.aiService.getTrainingData(r))
                .toList(),
            pageable,
            reportRepository.count()
        );
    }

    @GetMapping("/predict")
    public Report predict(
        @RequestParam(required = true) int country_id,
        @RequestParam(required = true) int pandemic_id,
        @RequestParam(required = true) Date predict_at
    ) {
        var report = reportRepository.findByDateAndCountryIdAndPandemicId(
            predict_at,
            country_id,
            pandemic_id
        );

        if (report != null) {
            return report;
        }

        return this.aiService.predict(
                predict_at,
                infectionRepository.findByPandemicIdAndCountryId(
                    pandemic_id,
                    country_id
                )
            );
    }
}
