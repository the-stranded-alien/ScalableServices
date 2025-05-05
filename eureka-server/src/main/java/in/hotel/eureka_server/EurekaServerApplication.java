package in.hotel.eureka_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication(exclude = {
		org.springframework.boot.actuate.autoconfigure.metrics.SystemMetricsAutoConfiguration.class,
		org.springframework.boot.actuate.autoconfigure.metrics.web.tomcat.TomcatMetricsAutoConfiguration.class,
		org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration.class
})
@EnableEurekaServer
public class EurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
	}

}
