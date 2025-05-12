package fr.mspr_api;

import fr.mspr_api.config.AiApiConfig;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.validation.annotation.Validated;

@SpringBootApplication
@ConfigurationPropertiesScan("fr.mspr_api.config")
public class MsprAPIApplication implements CommandLineRunner {

    @Autowired
    @Valid
    private final AiApiConfig aiApiConfig;

    MsprAPIApplication(@Validated AiApiConfig config) {
        this.aiApiConfig = config;
    }

    public static void main(String[] args) {
        SpringApplication.run(MsprAPIApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("AI API Key: " + aiApiConfig.getApiKey());
        System.out.println("AI API URL: " + aiApiConfig.getApiUrl());
    }
}
