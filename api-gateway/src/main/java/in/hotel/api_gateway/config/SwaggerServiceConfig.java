package in.hotel.api_gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration properties for Swagger services.
 * This class allows configuring which services should have their Swagger documentation
 * included in the API Gateway's Swagger UI.
 */
@Component
@ConfigurationProperties(prefix = "swagger")
public class SwaggerServiceConfig {

    /**
     * Map of service names to their enabled status.
     * Key: service name (e.g., "user-service")
     * Value: boolean indicating whether the service's Swagger documentation should be included
     */
    private Map<String, Boolean> services = new HashMap<>();

    /**
     * Default constructor initializing with default values
     */
    public SwaggerServiceConfig() {
        // Default values - all services enabled
        services.put("user-service", true);
        services.put("booking-service", true);
        services.put("hotel-service", true);
        services.put("notification-service", true);
    }

    public Map<String, Boolean> getServices() {
        return services;
    }

    public void setServices(Map<String, Boolean> services) {
        this.services = services;
    }

    /**
     * Check if a service is enabled
     * @param serviceName the name of the service
     * @return true if the service is enabled, false otherwise
     */
    public boolean isServiceEnabled(String serviceName) {
        return services.getOrDefault(serviceName, false);
    }
}