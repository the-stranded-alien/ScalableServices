package in.hotel.api_gateway.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(SwaggerServiceConfig.class)
@OpenAPIDefinition(
        info = @Info(
                title = "Hotel Management System API Gateway",
                version = "1.0",
                description = """
                        API Gateway for the Hotel Management System.

                        This gateway provides access to all microservices in the system:
                        - User Service: User management, authentication, and authorization
                        - Hotel Service: Hotel information, room types, and availability
                        - Booking Service: Booking management and reservations
                        - Notification Service: Email and notification management

                        Use the dropdown menu above to switch between services and explore their APIs.

                        You can enable or disable services in the Swagger UI by configuring the 
                        swagger.services.* properties in the application.properties file.
                        """,
                contact = @Contact(
                        name = "Hotel Management System Support",
                        email = "support@hotel-management.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        description = "JWT Authorization header using the Bearer scheme. Example: \"Authorization: Bearer {token}\""
)
public class OpenAPIConfig {

    @Value("${server.port:8084}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Local Development Server")
                ));
    }
}
