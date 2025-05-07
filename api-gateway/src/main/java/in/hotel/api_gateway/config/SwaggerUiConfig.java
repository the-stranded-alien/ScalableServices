package in.hotel.api_gateway.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration for Swagger UI to dynamically include or exclude services
 * based on configuration properties.
 */
@Configuration
@Primary
public class SwaggerUiConfig {

    private final SwaggerServiceConfig swaggerServiceConfig;
    private final Environment environment;

    @Autowired
    public SwaggerUiConfig(SwaggerServiceConfig swaggerServiceConfig, Environment environment) {
        this.swaggerServiceConfig = swaggerServiceConfig;
        this.environment = environment;
    }

    /**
     * Initialize the Swagger UI configuration based on enabled services.
     * This method sets system properties that will be used by SpringDoc to configure
     * which service APIs are included in the Swagger UI.
     */
    @PostConstruct
    public void init() {
        List<String> urls = new ArrayList<>();
        List<String> names = new ArrayList<>();

        // Always include API Gateway
        urls.add("/v3/api-docs");
        names.add("API Gateway");

        // Add enabled services
        if (swaggerServiceConfig.isServiceEnabled("user-service")) {
            urls.add("/user-service/v3/api-docs");
            names.add("User Service");
        }

        if (swaggerServiceConfig.isServiceEnabled("booking-service")) {
            urls.add("/booking-service/v3/api-docs");
            names.add("Booking Service");
        }

        if (swaggerServiceConfig.isServiceEnabled("hotel-service")) {
            urls.add("/hotel-service/v3/api-docs");
            names.add("Hotel Service");
        }

        if (swaggerServiceConfig.isServiceEnabled("notification-service")) {
            urls.add("/notification-service/v3/api-docs");
            names.add("Notification Service");
        }

        // Set system properties for SpringDoc to use
        for (int i = 0; i < urls.size(); i++) {
            System.setProperty("springdoc.swagger-ui.urls[" + i + "].url", urls.get(i));
            System.setProperty("springdoc.swagger-ui.urls[" + i + "].name", names.get(i));
        }
    }

    @Bean
    public GroupedOpenApi apiGatewayApi() {
        return GroupedOpenApi.builder()
                .group("api-gateway")
                .pathsToMatch("/api/**")
                .pathsToExclude("/api/user/**", "/api/booking/**", "/api/hotel/**", "/api/notification/**")
                .packagesToScan("in.hotel.api_gateway")
                .build();
    }


    @Bean
    public GroupedOpenApi userServiceApi() {
        if (!swaggerServiceConfig.isServiceEnabled("user-service")) {
            return null;
        }
        return GroupedOpenApi.builder()
                .group("user-service")
                .pathsToMatch("/api/user/**")
                .packagesToScan("in.hotel.user_service")
                .build();
    }

    @Bean
    public GroupedOpenApi bookingServiceApi() {
        if (!swaggerServiceConfig.isServiceEnabled("booking-service")) {
            return null;
        }
        return GroupedOpenApi.builder()
                .group("booking-service")
                .pathsToMatch("/api/booking/**")
                .packagesToScan("in.hotel.booking_service")
                .build();
    }

    @Bean
    public GroupedOpenApi hotelServiceApi() {
        if (!swaggerServiceConfig.isServiceEnabled("hotel-service")) {
            return null;
        }
        return GroupedOpenApi.builder()
                .group("hotel-service")
                .pathsToMatch("/api/hotel/**")
                .packagesToScan("in.hotel.hotel_service")
                .build();
    }

    @Bean
    public GroupedOpenApi notificationServiceApi() {
        if (!swaggerServiceConfig.isServiceEnabled("notification-service")) {
            return null;
        }
        return GroupedOpenApi.builder()
                .group("notification-service")
                .pathsToMatch("/api/notification/**")
                .packagesToScan("in.hotel.notification_service")
                .build();
    }
}
