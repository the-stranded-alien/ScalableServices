package in.hotel.api_gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A simple test controller to verify Swagger documentation.
 * This controller provides a basic endpoint that can be used to test
 * if controllers are properly discovered and documented in Swagger.
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    /**
     * A simple test endpoint that returns a greeting message.
     * @return A greeting message
     */
    @GetMapping
    public String test() {
        return "Hello from API Gateway Test Controller!";
    }
}