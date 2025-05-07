package in.hotel.api_gateway.controller;

import in.hotel.api_gateway.config.ServiceAccessConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for managing direct access to services through the API Gateway.
 * This controller provides endpoints to enable/disable direct access to services,
 * and to check the current status of service access.
 */
@RestController
@RequestMapping("/api/services")
@Tag(name = "Service Access", description = "Endpoints for managing direct access to services")
public class ServiceAccessController {

    private final ServiceAccessConfig serviceAccessConfig;

    @Autowired
    public ServiceAccessController(ServiceAccessConfig serviceAccessConfig) {
        this.serviceAccessConfig = serviceAccessConfig;
    }

    /**
     * Get the current status of all services
     * @return a map of service names to their enabled status
     */
    @GetMapping
    @Operation(
        summary = "Get service access status",
        description = "Returns the current status of direct access to all services",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved service access status",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    public ResponseEntity<Map<String, Boolean>> getServiceStatus() {
        return ResponseEntity.ok(serviceAccessConfig.getEnabled());
    }

    /**
     * Enable direct access to a service
     * @param serviceName the name of the service to enable
     * @return a success message with instructions
     */
    @PostMapping("/{serviceName}/enable")
    @Operation(
        summary = "Enable service access",
        description = "Provides instructions to enable direct access to the specified service",
        parameters = {
            @Parameter(name = "serviceName", description = "The name of the service to enable", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Instructions provided successfully",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Service not found",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    public ResponseEntity<Map<String, String>> enableService(@PathVariable String serviceName) {
        if (!isValidService(serviceName)) {
            return ResponseEntity.notFound().build();
        }

        serviceAccessConfig.enableService(serviceName);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Service " + serviceName + " marked as enabled for direct access");
        response.put("instructions", "To enable direct access to " + serviceName + ", uncomment the corresponding route in application.properties and restart the service.");

        if (serviceName.equals("booking-service")) {
            response.put("config_changes", "Uncomment the following lines in application.properties:\n" +
                    "spring.cloud.gateway.routes[1].id=booking-service\n" +
                    "spring.cloud.gateway.routes[1].uri=lb://booking-service\n" +
                    "spring.cloud.gateway.routes[1].predicates[0]=Path=/api/booking/**");
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Disable direct access to a service
     * @param serviceName the name of the service to disable
     * @return a success message with instructions
     */
    @PostMapping("/{serviceName}/disable")
    @Operation(
        summary = "Disable service access",
        description = "Provides instructions to disable direct access to the specified service",
        parameters = {
            @Parameter(name = "serviceName", description = "The name of the service to disable", required = true)
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Instructions provided successfully",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Service not found",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    public ResponseEntity<Map<String, String>> disableService(@PathVariable String serviceName) {
        if (!isValidService(serviceName)) {
            return ResponseEntity.notFound().build();
        }

        serviceAccessConfig.disableService(serviceName);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Service " + serviceName + " marked as disabled for direct access");
        response.put("instructions", "To disable direct access to " + serviceName + ", comment out the corresponding route in application.properties and restart the service.");

        if (serviceName.equals("booking-service")) {
            response.put("config_changes", "Comment out the following lines in application.properties:\n" +
                    "# spring.cloud.gateway.routes[1].id=booking-service\n" +
                    "# spring.cloud.gateway.routes[1].uri=lb://booking-service\n" +
                    "# spring.cloud.gateway.routes[1].predicates[0]=Path=/api/booking/**");
        } else if (serviceName.equals("user-service")) {
            response.put("config_changes", "Comment out the following lines in application.properties:\n" +
                    "# spring.cloud.gateway.routes[0].id=user-service\n" +
                    "# spring.cloud.gateway.routes[0].uri=lb://user-service\n" +
                    "# spring.cloud.gateway.routes[0].predicates[0]=Path=/api/user/**");
        } else if (serviceName.equals("hotel-service")) {
            response.put("config_changes", "Comment out the following lines in application.properties:\n" +
                    "# spring.cloud.gateway.routes[2].id=hotel-service\n" +
                    "# spring.cloud.gateway.routes[2].uri=lb://hotel-service\n" +
                    "# spring.cloud.gateway.routes[2].predicates[0]=Path=/api/hotel/**");
        } else if (serviceName.equals("notification-service")) {
            response.put("config_changes", "Comment out the following lines in application.properties:\n" +
                    "# spring.cloud.gateway.routes[3].id=notification-service\n" +
                    "# spring.cloud.gateway.routes[3].uri=lb://notification-service\n" +
                    "# spring.cloud.gateway.routes[3].predicates[0]=Path=/api/notification/**");
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Check if a service name is valid
     * @param serviceName the name of the service to check
     * @return true if the service name is valid, false otherwise
     */
    private boolean isValidService(String serviceName) {
        return serviceName != null && (
            serviceName.equals("user-service") ||
            serviceName.equals("booking-service") ||
            serviceName.equals("hotel-service") ||
            serviceName.equals("notification-service")
        );
    }
}
