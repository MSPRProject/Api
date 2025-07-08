package fr.mspr_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(
                authorize -> authorize.anyRequest().permitAll() // Allow all requests without authentication
            )
            .headers(headers ->
                headers
                    .contentTypeOptions(contentTypeOptions -> {})
                    .addHeaderWriter(
                        new StaticHeadersWriter(
                            "Cross-Origin-Opener-Policy",
                            "same-origin"
                        )
                    )
                    .addHeaderWriter(
                        new StaticHeadersWriter(
                            "Cross-Origin-Embedder-Policy",
                            "require-corp"
                        )
                    )
            );

        return http.build();
    }
}
