package fr.clementdessoude.accounting.client;

import fr.clementdessoude.accounting.config.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;

@Service
public class ProductClient extends BaseClient {
    private String username;
    private String password;

    public ProductClient(
        ApplicationProperties applicationProperties,
        @Value("${application.services.product.username}") String username,
        @Value("${application.services.product.password}") String password
    ) {
        super(applicationProperties);
        this.serviceName = "product";
        this.username = username;
        this.password = password;
    }

    public Long getAmountOrderedByCustomerBelowAge(Long age) {
         return client
            .get()
            .uri(String.format("/customer/below/%s/transactions/sum", age))
            .headers(headers -> headers.setBasicAuth(username, password))
            .retrieve()
            .bodyToMono(Long.class)
            .block();
    }

    public Long getAmountOrderedByCustomerBelowAgeV2(Long age) {
        return client
            .get()
            .uri(String.format("/customer/below/%s/transactions/sum/v2", age))
            .headers(headers -> headers.setBasicAuth(username, password))
            .retrieve()
            .bodyToMono(Long.class)
            .block();
    }
}
