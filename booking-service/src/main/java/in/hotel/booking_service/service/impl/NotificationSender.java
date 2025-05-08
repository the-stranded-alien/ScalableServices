package in.hotel.booking_service.service.impl;

import in.hotel.common_library.models.AuditEvent;
import in.hotel.common_library.models.NotificationEvent;

public interface NotificationSender {

    void sendNotification(NotificationEvent event);
    void sendAudit(AuditEvent event);
}