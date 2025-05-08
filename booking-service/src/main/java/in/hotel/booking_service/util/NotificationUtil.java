package in.hotel.booking_service.util;

import in.hotel.common_library.models.NotificationEvent;
import in.hotel.common_library.models.AuditEvent;
import in.hotel.booking_service.service.NotificationSenderImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class NotificationUtil {

    private final NotificationSenderImpl notificationSender;

    public void sendBookingViewNotification() {
        NotificationEvent event = EventFactory.createNotificationEvent("BOOKINGS_VIEWED", "All bookings list was viewed");
        notificationSender.sendNotification(event);
    }

    public void sendAllBookingViewedAudit() {
        AuditEvent event = EventFactory.createAuditEvent("BOOKINGS_VIEWED", "All bookings list was viewed");
        notificationSender.sendAudit(event);
    }

    public void sendCreateBookingAudit(String bookingId) {
        AuditEvent event = EventFactory.createAuditEvent(
                "BOOKINGS_CREATED",
                String.format("New booking created with Id: %s", bookingId));
        notificationSender.sendAudit(event);
    }

    public void sendCreateBookingNotification(String bookingId) {
        NotificationEvent event = EventFactory.createNotificationEvent(
                "BOOKINGS_CREATED",
                String.format("New booking created with Id: %s", bookingId));
        notificationSender.sendNotification(event);
    }

}
