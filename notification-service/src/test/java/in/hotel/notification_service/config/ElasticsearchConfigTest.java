package in.hotel.notification_service.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.ElasticsearchTransport;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ElasticsearchConfigTest {

    private ElasticsearchConfig elasticsearchConfig;

    @BeforeEach
    void setUp() {
        elasticsearchConfig = new ElasticsearchConfig();
        ReflectionTestUtils.setField(elasticsearchConfig, "elasticsearchUri", "http://localhost:9200");
        ReflectionTestUtils.setField(elasticsearchConfig, "username", "");
        ReflectionTestUtils.setField(elasticsearchConfig, "password", "");
    }

    @Test
    void testRestClientCreation() {
        assertDoesNotThrow(() -> {
            RestClient restClient = elasticsearchConfig.restClient();
            assertNotNull(restClient);
        });
    }
}
