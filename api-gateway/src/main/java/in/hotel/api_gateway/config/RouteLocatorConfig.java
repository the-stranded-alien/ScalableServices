package in.hotel.api_gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration for dynamically creating routes based on service access settings.
 * This class replaces the static route configuration in application.properties.
 */
@Configuration
public class RouteLocatorConfig {

    private final ServiceAccessConfig serviceAccessConfig;

    @Autowired
    public RouteLocatorConfig(ServiceAccessConfig serviceAccessConfig) {
        this.serviceAccessConfig = serviceAccessConfig;
    }

    /**
     * Creates a RouteLocator bean that dynamically configures routes based on
     * the service access configuration.
     *
     * @param builder the RouteLocatorBuilder
     * @return a RouteLocator with dynamically configured routes
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();

        // User Service Route
        if (serviceAccessConfig.isServiceEnabled("user-service")) {
            routes = routes.route("user-service",
                    r -> r.path("/api/user/**")
                          .uri("lb://user-service"));
        }

        // Booking Service Route
        if (serviceAccessConfig.isServiceEnabled("booking-service")) {
            routes = routes.route("booking-service",
                    r -> r.path("/api/booking/**")
                          .uri("lb://booking-service"));
        }

        // Hotel Service Route
        if (serviceAccessConfig.isServiceEnabled("hotel-service")) {
            routes = routes.route("hotel-service",
                    r -> r.path("/api/hotel/**")
                          .uri("lb://hotel-service"));
        }

        // Notification Service Route
        if (serviceAccessConfig.isServiceEnabled("notification-service")) {
            routes = routes.route("notification-service",
                    r -> r.path("/api/notification/**")
                          .uri("lb://notification-service"));
        }

        // Swagger Documentation Routes - always enabled for API documentation

        // User Service Swagger
        routes = routes.route("user-service-swagger",
                r -> r.path("/user-service/v3/api-docs/**")
                      .filters(f -> f.rewritePath("/user-service/(?<segment>.*)", "/${segment}"))
                      .uri("lb://user-service"));

        // Booking Service Swagger
        routes = routes.route("booking-service-swagger",
                r -> r.path("/booking-service/v3/api-docs/**")
                      .filters(f -> f.rewritePath("/booking-service/(?<segment>.*)", "/${segment}"))
                      .uri("lb://booking-service"));

        // Hotel Service Swagger
        routes = routes.route("hotel-service-swagger",
                r -> r.path("/hotel-service/v3/api-docs/**")
                      .filters(f -> f.rewritePath("/hotel-service/(?<segment>.*)", "/${segment}"))
                      .uri("lb://hotel-service"));

        // Notification Service Swagger
        routes = routes.route("notification-service-swagger",
                r -> r.path("/notification-service/v3/api-docs/**")
                      .filters(f -> f.rewritePath("/notification-service/(?<segment>.*)", "/${segment}"))
                      .uri("lb://notification-service"));

        // Swagger UI route
        routes = routes.route("swagger-ui",
                r -> r.path("/swagger-ui.html", "/swagger-ui/**")
                      .uri("http://localhost:8084"));

        // Swagger webjars route
        routes = routes.route("webjars",
                r -> r.path("/webjars/**")
                      .uri("http://localhost:8084"));

        // API Gateway's own API docs
        routes = routes.route("api-gateway-docs",
                r -> r.path("/v3/api-docs/**")
                      .uri("http://localhost:8084"));

        return routes.build();
    }
}
