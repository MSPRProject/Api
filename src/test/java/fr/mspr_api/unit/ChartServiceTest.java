package fr.mspr_api.unit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import fr.mspr_api.component.Pandemic;
import fr.mspr_api.repository.InfectionRepository;
import fr.mspr_api.repository.PandemicRepository;
import fr.mspr_api.repository.ReportRepository;
import fr.mspr_api.service.ChartService;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ChartServiceTest {

    @Mock
    private InfectionRepository infectionRepository;

    @Mock
    private PandemicRepository pandemicRepository;

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ChartService chartService;

    private Pandemic pandemic;

    @BeforeEach
    public void setUp() {
        pandemic = new Pandemic();
        pandemic.setId(1);
        pandemic.setName("COVID-19");
        
        chartService.pruneCaches();
    }

    @Test
    public void testPruneCachesDoesNotThrowException() {        
        chartService.pruneCaches();

        assertTrue(true, "La méthode pruneCaches s'est exécutée sans erreur");
    }

    @Test
    public void testGetTop10CountriesWithPandemicThrowsChartGeneratingException() throws Exception {        
        ChartService.ChartGeneratingException exception = assertThrows(
            ChartService.ChartGeneratingException.class,
            () -> chartService.getTop10CountriesByCasesOrDeaths(Optional.of(pandemic))
        );
        
        assertTrue(exception.getMessage().contains("Chart is being generated"));
    }
}
