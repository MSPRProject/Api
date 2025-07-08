package fr.mspr_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                    .addMapping("/**")
                    .allowedOrigins("http://localhost:4200")
                    .allowedMethods("*") // GET, POST, PUT, DELETE, etc.
                    .allowedHeaders("*")
                    .allowCredentials(true);
            }
        };
    }

    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer() {
        return new RepositoryRestConfigurer() {
            @Override
            public void configureRepositoryRestConfiguration(
                RepositoryRestConfiguration config,
                CorsRegistry cors
            ) {
                cors
                    .addMapping("/**")
                    .allowedOrigins("http://localhost:4200")
                    .allowedMethods("*") // GET, POST, PUT, DELETE, etc.
                    .allowedHeaders("*")
                    .allowCredentials(true);
            }
        };
    }
}
