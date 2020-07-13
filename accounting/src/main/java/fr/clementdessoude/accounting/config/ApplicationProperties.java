package fr.clementdessoude.accounting.config;

import lombok.Data;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("application")
@Data
public class ApplicationProperties {
    private Map<String, String> oauth;
    private Map<String, ExternalService> services;

    @Data
    public static class ExternalService {
        private String host;
        private String port;
        private String endpoint;
        private Boolean tls;
    }
}
