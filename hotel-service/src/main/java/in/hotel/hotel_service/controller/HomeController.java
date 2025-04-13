package in.hotel.hotel_service.controller;

import in.hotel.hotel_service.model.HealthResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class HomeController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Operation(summary = "Test hotel service")
    @GetMapping("/test")
    public HealthResponse checkApplicationHealth() {
        return new HealthResponse(
                "Hotel Service",
                "UP",
                "Hotel service is running and healthy."
        );
    }

    @Operation(summary = "Test hotel service database (mongo-db)")
    @GetMapping("/test-db")
    public HealthResponse checkDatabaseHealth() {
        try {
            mongoTemplate.executeCommand("{ ping: 1 }");
            return new HealthResponse(
                    "MongoDB",
                    "UP",
                    "MongoDB connection successful."
            );
        } catch (Exception e) {
            return new HealthResponse(
                    "MongoDB",
                    "DOWN",
                    "MongoDB is not reachable: " + e.getMessage()
            );
        }
    }
}
