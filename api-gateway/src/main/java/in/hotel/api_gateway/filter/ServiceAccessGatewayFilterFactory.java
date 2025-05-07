package in.hotel.api_gateway.filter;

import in.hotel.api_gateway.config.ServiceAccessConfig;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/**
 * Gateway filter factory for controlling direct access to services.
 * This filter checks if a service is enabled for direct access based on the
 * configuration in ServiceAccessConfig.
 */
@Component
public class ServiceAccessGatewayFilterFactory extends AbstractGatewayFilterFactory<ServiceAccessGatewayFilterFactory.Config> {

    private final ServiceAccessConfig serviceAccessConfig;

    public ServiceAccessGatewayFilterFactory(ServiceAccessConfig serviceAccessConfig) {
        super(Config.class);
        this.serviceAccessConfig = serviceAccessConfig;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String serviceName = config.getServiceName();
            
            if (!serviceAccessConfig.isServiceEnabled(serviceName)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, 
                    "Direct access to " + serviceName + " is disabled. Use the API Gateway's service access endpoints to enable it.");
            }
            
            return chain.filter(exchange);
        };
    }

    /**
     * Configuration class for the ServiceAccessGatewayFilterFactory.
     */
    public static class Config {
        private String serviceName;

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }
    }
}