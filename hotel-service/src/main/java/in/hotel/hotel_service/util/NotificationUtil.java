package in.hotel.hotel_service.util;

import in.hotel.common_library.models.NotificationEvent;
import in.hotel.common_library.models.AuditEvent;
import in.hotel.hotel_service.service.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class NotificationUtil {

    private final NotificationSender notificationSender;

    public void sendHotelViewNotification() {
        NotificationEvent event = EventFactory.createNotificationEvent("HOTELS_VIEWED", "All hotels list was viewed");
        notificationSender.sendNotification(event);
    }

    public void sendAllHotelViewedAudit() {
        AuditEvent event = EventFactory.createAuditEvent("HOTELS_VIEWED", "All hotels list was viewed");
        notificationSender.sendAudit(event);
    }

    public void sendCreateHotelAudit(String hotelId, String hotelName) {
        AuditEvent event = EventFactory.createAuditEvent(
                "HOTELS_CREATED",
                String.format("New hotel created with Id: %s, Name: %s", hotelId, hotelName));
        notificationSender.sendAudit(event);
    }

    public void sendCreateHotelNotification(String hotelId, String hotelName) {
        NotificationEvent event = EventFactory.createNotificationEvent(
                "HOTELS_CREATED",
                String.format("New hotel created with Id: %s, Name: %s by User", hotelId, hotelName));
        notificationSender.sendNotification(event);
    }

    public void sendUpdateHotelAudit(String hotelId, String hotelName) {
        AuditEvent event = EventFactory.createAuditEvent(
                "HOTELS_UPDATED",
                String.format("Hotel with Id: %s, Name: %s was updated", hotelId, hotelName));
        notificationSender.sendAudit(event);
    }

    public void sendUpdateHotelNotification(String hotelId, String hotelName) {
        NotificationEvent event = EventFactory.createNotificationEvent(
                "HOTELS_UPDATED",
                String.format("Hotel with Id: %s, Name: %s was updated by User", hotelId, hotelName));
        notificationSender.sendNotification(event);
    }
    public void sendDeleteHotelAudit(String hotelId) {
        AuditEvent event = EventFactory.createAuditEvent(
                "HOTELS_DELETED",
                String.format("Hotel with Id: %s deleted", hotelId));
        notificationSender.sendAudit(event);
    }
    public void sendDeleteHotelNotification(String hotelId) {
        NotificationEvent event = EventFactory.createNotificationEvent(
                "HOTELS_DELETED",
                String.format("Hotel with Id: %s was deleted by User", hotelId));
        notificationSender.sendNotification(event);
    }
}
