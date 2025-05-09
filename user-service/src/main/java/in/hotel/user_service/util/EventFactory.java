package in.hotel.user_service.util;

import in.hotel.common_library.models.AuditEvent;
import in.hotel.common_library.models.CustomUserPrincipal;
import in.hotel.common_library.models.NotificationEvent;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

public class EventFactory {

    private final static String SERVICE_SOURCE = "User Service";

    public static NotificationEvent createNotificationEvent(String type, String message) {
        return NotificationEvent.builder()
                .type(type)
                .message(message)
                .timestamp(LocalDateTime.now())
                .serviceSource(SERVICE_SOURCE)
                .build();
    }

    public static AuditEvent createAuditEvent(String type, String message) {
        return AuditEvent.builder()
                .type(type)
                .message(message)
                .timestamp(LocalDateTime.now())
                .serviceSource(SERVICE_SOURCE)
                .build();
    }
}
