package in.hotel.api_gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration properties for direct service access.
 * This class allows configuring which services should be directly accessible
 * through the API Gateway.
 */
@Component
@ConfigurationProperties(prefix = "service.access.enabled")
public class ServiceAccessConfig {

    /**
     * Map of service names to their enabled status.
     * Key: service name (e.g., "user-service")
     * Value: boolean indicating whether the service is directly accessible
     */
    private Map<String, Boolean> enabled = new HashMap<>();

    /**
     * Default constructor initializing with default values
     */
    public ServiceAccessConfig() {
        // Default values - user service enabled, booking service disabled
        enabled.put("user-service", true);
        enabled.put("booking-service", false);
        enabled.put("hotel-service", true);
        enabled.put("notification-service", true);
    }

    public Map<String, Boolean> getEnabled() {
        return enabled;
    }

    public void setEnabled(Map<String, Boolean> enabled) {
        this.enabled = enabled;
    }

    /**
     * Check if a service is enabled for direct access
     * @param serviceName the name of the service
     * @return true if the service is enabled for direct access, false otherwise
     */
    public boolean isServiceEnabled(String serviceName) {
        return enabled.getOrDefault(serviceName, false);
    }

    /**
     * Enable a service for direct access
     * @param serviceName the name of the service
     */
    public void enableService(String serviceName) {
        enabled.put(serviceName, true);
    }

    /**
     * Disable a service for direct access
     * @param serviceName the name of the service
     */
    public void disableService(String serviceName) {
        enabled.put(serviceName, false);
    }
}
