package in.hotel.notification_service.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Filter that ensures all requests come through the API Gateway.
 * It checks the source IP address of the request and only allows requests from
 * the API Gateway's IP address or from localhost (for development purposes).
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiGatewayFilter extends OncePerRequestFilter {

    @Value("${api.gateway.ip:localhost}")
    private String apiGatewayIp;

    @Value("${api.gateway.port:8084}")
    private String apiGatewayPort;

    private static final List<String> ALLOWED_IPS = Arrays.asList(
            "127.0.0.1",      // localhost IPv4
            "0:0:0:0:0:0:0:1" // localhost IPv6
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String remoteAddr = request.getRemoteAddr();
        
        // Allow requests from localhost (for development) or from the API Gateway
        if (ALLOWED_IPS.contains(remoteAddr) || remoteAddr.equals(apiGatewayIp)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access denied. All requests must go through the API Gateway.");
        }
    }
}