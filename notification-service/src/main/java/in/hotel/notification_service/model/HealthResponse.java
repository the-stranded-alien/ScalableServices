package in.hotel.notification_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HealthResponse {
    private String service;
    private String status;
    private String message;
}
