package fr.mspr_api.config;

import jakarta.validation.constraints.NotBlank;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "ai")
@ConfigurationPropertiesScan
@Validated
public class AiApiConfig {

    @NotBlank
    private String apiKey;

    @NotBlank
    private String apiUrlStr;

    private URI apiUri;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiUrl() {
        return apiUrlStr;
    }

    public void setApiUrl(String apiUrl) throws URISyntaxException {
        this.apiUrlStr = apiUrl;
        this.apiUri = new URI(apiUrl);
    }

    public URI getPromptUrl() {
        return this.apiUri.resolve("/prompt");
    }
}
