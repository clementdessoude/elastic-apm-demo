package fr.clementdessoude.accounting.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class ProductClient {
    @Value("${application.services.product.endpoint}")
    private String endpoint;

    @Value("${application.services.product.username}")
    private String username;

    @Value("${application.services.product.password}")
    private String password;

    private WebClient client;

    @PostConstruct
    public void setup() {
        this.client = WebClient.create(endpoint);
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
