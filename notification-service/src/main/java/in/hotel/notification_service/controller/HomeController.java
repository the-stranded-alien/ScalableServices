package in.hotel.notification_service.controller;

import in.hotel.notification_service.model.HealthResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
public class HomeController {

    @Value("${spring.elasticsearch.uris}")
    private String elasticsearchUri;

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaBootstrapServers;

    @Operation(summary = "Test notification service")
    @GetMapping("/test")
    public HealthResponse checkApplicationHealth() {
        return new HealthResponse("Notification Service", "UP", "Service is running");
    }

    @Operation(summary = "Test notification service database (elastic-search)")
    @GetMapping("/test-db")
    public HealthResponse checkDatabaseHealth() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String elasticHealthUrl = elasticsearchUri + "/_cluster/health";
            String elasticResponse = restTemplate.getForObject(elasticHealthUrl, String.class);
            return new HealthResponse("Elastic-Search", "UP", "Elasticsearch is running healthy. Response: " + elasticResponse);
        } catch (Exception e) {
            return new HealthResponse("Elastic-Search", "DOWN", "Elasticsearch is not reachable: " + e.getMessage());
        }
    }

    @Operation(summary = "Test notification service IPC (Kafka)")
    @GetMapping("/test-ipc")
    public HealthResponse checkIpcHealth() {
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaBootstrapServers);
        props.put("group.id", "health-check");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {

            Future<Void> task = java.util.concurrent.CompletableFuture.runAsync(consumer::listTopics);

            task.get(5, TimeUnit.SECONDS);

            return new HealthResponse("Kafka", "UP", "Kafka is running");

        } catch (TimeoutException e) {
            return new HealthResponse("Kafka", "DOWN", "Kafka is not reachable. Timeout: " + e.getMessage());
        } catch (Exception e) {
            return new HealthResponse("Kafka", "DOWN", "Kafka is not reachable: " + e.getMessage());
        }
    }
}
