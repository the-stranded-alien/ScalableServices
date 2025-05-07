package in.hotel.api_gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Configuration for dynamically enabling/disabling routes based on service access settings.
 * This class uses system properties to control which routes are enabled.
 */
@Configuration
public class DynamicRouteConfig {

    private final ServiceAccessConfig serviceAccessConfig;
    private final Environment environment;

    @Autowired
    public DynamicRouteConfig(ServiceAccessConfig serviceAccessConfig, Environment environment) {
        this.serviceAccessConfig = serviceAccessConfig;
        this.environment = environment;
    }

    /**
     * CommandLineRunner bean that sets system properties to enable/disable routes
     * based on the service access configuration.
     * 
     * @return a CommandLineRunner that sets system properties
     */
    @Bean
    public CommandLineRunner routeConfigurer() {
        return args -> {
            // User Service
            if (!serviceAccessConfig.isServiceEnabled("user-service")) {
                System.setProperty("spring.cloud.gateway.routes[0].id", "disabled-user-service");
                System.out.println("User service direct access disabled");
            } else {
                System.out.println("User service direct access enabled");
            }

            // Booking Service
            if (!serviceAccessConfig.isServiceEnabled("booking-service")) {
                // Comment out the booking service route by setting a system property
                System.setProperty("spring.cloud.gateway.routes[1].id", "disabled-booking-service");
                System.out.println("Booking service direct access disabled");
            } else {
                // Uncomment the booking service route by setting system properties
                System.setProperty("spring.cloud.gateway.routes[1].id", "booking-service");
                System.setProperty("spring.cloud.gateway.routes[1].uri", "lb://booking-service");
                System.setProperty("spring.cloud.gateway.routes[1].predicates[0]", "Path=/api/booking/**");
                System.out.println("Booking service direct access enabled");
            }

            // Hotel Service
            if (!serviceAccessConfig.isServiceEnabled("hotel-service")) {
                System.setProperty("spring.cloud.gateway.routes[2].id", "disabled-hotel-service");
                System.out.println("Hotel service direct access disabled");
            } else {
                System.out.println("Hotel service direct access enabled");
            }

            // Notification Service
            if (!serviceAccessConfig.isServiceEnabled("notification-service")) {
                System.setProperty("spring.cloud.gateway.routes[3].id", "disabled-notification-service");
                System.out.println("Notification service direct access disabled");
            } else {
                System.out.println("Notification service direct access enabled");
            }
        };
    }
}