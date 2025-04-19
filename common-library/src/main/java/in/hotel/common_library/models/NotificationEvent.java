package in.hotel.common_library.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationEvent {
    private String id;
    private String type;
    private String message;
    private String userId;
    private LocalDateTime timestamp;
    private String serviceSource;
}
