package fr.mspr_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MsprAPIApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MsprAPIApplication.class, args);
    }
}
