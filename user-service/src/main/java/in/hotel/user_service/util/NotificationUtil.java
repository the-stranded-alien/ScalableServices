package in.hotel.user_service.util;

import in.hotel.common_library.models.NotificationEvent;
import in.hotel.common_library.models.AuditEvent;
import in.hotel.user_service.service.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class NotificationUtil {

    private final NotificationSender notificationSender;

    public void sendAllUserViewedAudit() {
        AuditEvent event = EventFactory.createAuditEvent("USERS_VIEWED", "All bookings list was viewed");
        notificationSender.sendAudit(event);
    }

    public void sendUserLoginAudit(String username) {
        AuditEvent event = EventFactory.createAuditEvent("USER_LOGIN", String.format("User %s logged in", username));
        notificationSender.sendAudit(event);
    }

    public void sendCreateUserAudit(String username) {
        AuditEvent event = EventFactory.createAuditEvent(
                "USER_CREATED",
                String.format("New user created with username: %s", username));
        notificationSender.sendAudit(event);
    }

    public void sendCreateUserNotification(String username) {
        NotificationEvent event = EventFactory.createNotificationEvent(
                "USER_CREATED",
                String.format("New user created with username: %s", username));
        notificationSender.sendNotification(event);
    }



}
