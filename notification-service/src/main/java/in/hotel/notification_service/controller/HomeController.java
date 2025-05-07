package in.hotel.notification_service.controller;

import in.hotel.notification_service.model.HealthResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController("/api/notification")
public class HomeController {

    @Value("${spring.elasticsearch.uris}")
    private String elasticsearchUri;

    private final ConnectionFactory connectionFactory;

    @Autowired
    public HomeController(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

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

    @Operation(summary = "Test notification service IPC (RabbitMQ)")
    @GetMapping("/test-ipc")
    public HealthResponse checkIpcHealth() {
        try {
            boolean isConnected = connectionFactory.createConnection().isOpen();

            if (isConnected) {
                return new HealthResponse("RabbitMQ", "UP", "RabbitMQ is running");
            } else {
                return new HealthResponse("RabbitMQ", "DOWN", "RabbitMQ connection is closed");
            }
        } catch (Exception e) {
            return new HealthResponse("RabbitMQ", "DOWN", "RabbitMQ is not reachable: " + e.getMessage());
        }
    }
}
