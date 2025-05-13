package fr.mspr_api.controller;

import fr.mspr_api.component.Report;
import fr.mspr_api.repository.ReportRepository;
import fr.mspr_api.service.AiService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
class AiController {

    @Autowired
    private final AiService aiService;

    @Autowired
    private final ReportRepository reportRepository;

    public AiController(
        AiService aiService,
        ReportRepository reportRepository
    ) {
        this.aiService = aiService;
        this.reportRepository = reportRepository;
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
}
