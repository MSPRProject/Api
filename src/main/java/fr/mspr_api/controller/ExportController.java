package fr.mspr_api.controller;

import fr.mspr_api.service.ExportService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/export")
public class ExportController {

    @Autowired
    private ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<Object>>> exportReportsAsJson() {
        return ResponseEntity.ok()
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=export.json"
            )
            .contentType(MediaType.APPLICATION_JSON)
            .body(exportService.exportReportsAsJson());
    }

    @GetMapping(value = "/export", produces = "text/csv")
    public ResponseEntity<String> exportReportsAsCsv() {
        return ResponseEntity.ok()
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=export.csv"
            )
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(exportService.exportReportsAsCsv());
    }
}
