package fr.mspr_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "fr.mspr_api.component") // Remplacez par le package contenant vos entités

public class MsprAPIApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsprAPIApplication.class, args);
	}

}
