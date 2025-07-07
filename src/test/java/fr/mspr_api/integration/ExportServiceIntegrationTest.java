package fr.mspr_api.integration;

import fr.mspr_api.service.ExportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class ExportServiceIntegrationTest {

    @Autowired
    private ExportService exportService;

    @Test
    public void testExportReportsAsCsv() {
        String csvResult = exportService.exportReportsAsCsv();
        assertThat(csvResult).isNotNull();
        assertThat(csvResult.trim()).isNotEmpty();
        
        String[] lines = csvResult.split("\n");
        String header = lines[0];

        assertThat(header).isEqualTo(
            "id,continent,country_name,country_iso3,pandemic_name,pandemic_pathogen,pandemic_start_date,pandemic_end_date,pandemic_description,pandemic_notes,report_date,report_new_cases,report_new_deaths"
        );
        assertThat(lines.length).isGreaterThan(1);

        for (int i = 1; i < lines.length; i++) {
            String[] fields = lines[i].split(",");
            assertThat(fields.length).isGreaterThanOrEqualTo(13);
        }
    }
}
