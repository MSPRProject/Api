package fr.mspr_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("fr.mspr_api.config")
public class MsprAPIApplication {

    MsprAPIApplication() {}

    public static void main(String[] args) {
        SpringApplication.run(MsprAPIApplication.class, args);
    }
}
