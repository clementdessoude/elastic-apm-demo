package fr.clementdessoude.accounting.client;

import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Span;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.PostConstruct;

import fr.clementdessoude.accounting.config.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class BaseClient {
    protected final ApplicationProperties applicationProperties;

    protected WebClient client;
    protected String serviceName;

    @PostConstruct
    protected void setup() {
        this.client =
            WebClient.builder().filter(addTracingHeaders()).baseUrl(getEndpoint()).build();
    }

    protected String getEndpoint() {
        ApplicationProperties.ExternalService service = applicationProperties
            .getServices()
            .get(serviceName);

        if (service.getEndpoint() != null) {
            return service.getEndpoint();
        }

        return (
            (Boolean.TRUE.equals(service.getTls()) ? "https://" : "http://") +
                service.getHost() +
                ":" +
                service.getPort()
        );
    }

    private ExchangeFilterFunction addTracingHeaders() {
        return (clientRequest, next) -> {
            Span span = ElasticApm.currentSpan();

            AtomicReference<String> headerName = new AtomicReference<>();
            AtomicReference<String> headerValue = new AtomicReference<>();
            span.injectTraceHeaders(
                (name, value) -> {
                    headerName.set(name);
                    headerValue.set(value);
                }
            );

            ClientRequest filtered = ClientRequest
                .from(clientRequest)
                .header(headerName.get(), headerValue.get())
                .build();

            return next.exchange(filtered);
        };
    }
}

