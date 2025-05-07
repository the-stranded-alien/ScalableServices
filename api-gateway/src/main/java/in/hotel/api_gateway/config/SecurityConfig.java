package in.hotel.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        // Allow login and registration endpoints
                        .pathMatchers("/api/user/login", "/api/user/register").permitAll()
                        // Allow test endpoint
                        .pathMatchers("/api/test").permitAll()
                        // Allow service access to endpoints
                        .pathMatchers("/api/services/**").permitAll()
                        // Allow Swagger UI and related docs for all services
                        .pathMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/swagger-ui/webjars/swagger-ui/**",
                                "/webjars/**",
                                "/v3/api-docs/**",
                                "/user-service/v3/api-docs/**",
                                "/booking-service/v3/api-docs/**",
                                "/hotel-service/v3/api-docs/**",
                                "/notification-service/v3/api-docs/**"
                        ).permitAll()
                        .anyExchange().authenticated()
                )
                // Disable sessions since we're using JWT
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                // Disable the default login form
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .build();
    }
}
