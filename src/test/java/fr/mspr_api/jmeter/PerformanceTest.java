package fr.mspr_api.jmeter;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import us.abstracta.jmeter.javadsl.core.TestPlanStats;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PerformanceTest {
    
    @LocalServerPort
    private int port;
    
    @Test
    public void testGetCountriesEndpoint() throws Exception {
        TestPlanStats stats = testPlan(
            threadGroup(10, 100, 
                httpSampler("http://localhost:" + port + "/countries")
                    .method("GET")
            )
        ).run();

        assertTrue(stats.overall().errorsCount() == 0, "Il y a eu des erreurs durant le test");
    }

    // @Test
    // public void testPostCountryEndpoint() throws Exception {
    //     TestPlanStats stats = testPlan(
    //         threadGroup(10, 100, 
    //             httpSampler("http://localhost:" + port + "/countries")
    //                 .method("POST")
    //                 .body("{\"continent\": \"EUROPE\", \"name\": \"Test Country ${__threadNum}\", \"iso3\": \"TC${__Random(1,999)}\"}")
    //                 .header("Content-Type", "application/json")
    //         )
    //     ).run();

    //     assertTrue(stats.overall().errorsCount() == 0, "Il y a eu des erreurs durant le test");
    // }
}
