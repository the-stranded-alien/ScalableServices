package in.hotel.notification_service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMQListenerService {

    @RabbitListener(queues = "hotel-notifications")
    public void handleHotelNotification(String message) {
        log.info("Received hotel notification message='{}'", message);
    }

    @RabbitListener(queues = "user-notifications")
    public void handleUserNotification(String message) {
        log.info("Received user notification message='{}'", message);
    }

    @RabbitListener(queues = "booking-notifications")
    public void handleBookingNotification(String message) {
        log.info("Received booking notification message='{}'", message);
    }

    @RabbitListener(queues = "hotel-audit")
    public void handleHotelAudit(String message) {
        log.info("Received hotel audit message='{}'", message);
    }

    @RabbitListener(queues = "user-audit")
    public void handleUserAudit(String message) {
        log.info("Received user audit message='{}'", message);
    }

    @RabbitListener(queues = "booking-audit")
    public void handleBookingAudit(String message) {
        log.info("Received booking audit message='{}'", message);
    }
}