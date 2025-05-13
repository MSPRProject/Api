package fr.mspr_api;

import fr.mspr_api.config.AiApiConfig;
import fr.mspr_api.repository.InfectionRepository;
import fr.mspr_api.service.AiService;
import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("fr.mspr_api.config")
public class MsprAPIApplication implements CommandLineRunner {

    @Autowired
    private final AiApiConfig aiApiConfig;

    @Autowired
    private final AiService aiService;

    @Autowired
    private final InfectionRepository infectionRepository;

    MsprAPIApplication(
        AiApiConfig config,
        AiService service,
        InfectionRepository infectionRepository
    ) {
        this.aiApiConfig = config;
        this.aiService = service;
        this.infectionRepository = infectionRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(MsprAPIApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(
            "Prompt: " +
            aiService.prompt(
                new Date(new java.util.Date().getTime()),
                this.infectionRepository.findById(1).get()
            )
        );
        System.out.println("AI API URL: " + aiApiConfig.getApiUrl());
    }
}
