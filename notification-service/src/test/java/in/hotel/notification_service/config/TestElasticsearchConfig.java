package in.hotel.notification_service.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import in.hotel.notification_service.repository.AuditRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
public class TestElasticsearchConfig {

    @Bean
    @Primary
    public ElasticsearchClient elasticsearchClient() {
        ElasticsearchClient mockClient = mock(ElasticsearchClient.class);
        try {
            // Mock any necessary methods
            when(mockClient.ping()).thenReturn(new BooleanResponse(true));
        } catch (Exception e) {
            // Ignore exceptions in test setup
        }
        return mockClient;
    }

    @Bean
    @Primary
    public ElasticsearchOperations elasticsearchOperations() {
        return mock(ElasticsearchOperations.class);
    }

    @Bean
    @Primary
    public ElasticsearchTemplate elasticsearchTemplate() {
        return mock(ElasticsearchTemplate.class);
    }

    @Bean
    @Primary
    public AuditRepository auditRepository() {
        return mock(AuditRepository.class);
    }
}
