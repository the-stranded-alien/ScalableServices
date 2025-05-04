package in.hotel.common_library.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private String id;
    private String type;
    private String message;
    private String userId;
    private String userEmail;
    private LocalDateTime timestamp;
    private String serviceSource;
}
