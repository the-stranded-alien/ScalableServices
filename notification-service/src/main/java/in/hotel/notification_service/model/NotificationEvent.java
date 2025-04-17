package in.hotel.notification_service.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationEvent {
    private String id;
    private String type;
    private String message;
    private String userId;
    private LocalDateTime timestamp;
    private String serviceSource;
}
