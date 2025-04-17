package in.hotel.notification_service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaListenerService {

    @KafkaListener(topics = "hotel-notifications", groupId = "${spring.kafka.consumer.group-id}")
    public void handleHotelNotification(String message) {
        log.info("Received hotel notification message='{}'", message);
    }

    @KafkaListener(topics = "user-notifications", groupId = "${spring.kafka.consumer.group-id}")
    public void handleUserNotification(String message) {
        log.info("Received user notification message='{}'", message);
    }

    @KafkaListener(topics = "booking-notifications", groupId = "${spring.kafka.consumer.group-id}")
    public void handleBookingNotification(String message) {
        log.info("Received booking notification message='{}'", message);
    }

    @KafkaListener(topics = "hotel-audit", groupId = "${spring.kafka.consumer.group-id}")
    public void handleHotelAudit(String message) {
        log.info("Received hotel audit message='{}'", message);
    }

    @KafkaListener(topics = "user-audit", groupId = "${spring.kafka.consumer.group-id}")
    public void handleUserAudit(String message) {
        log.info("Received user audit message='{}'", message);
    }

    @KafkaListener(topics = "booking-audit", groupId = "${spring.kafka.consumer.group-id}")
    public void handleBookingAudit(String message) {
        log.info("Received booking audit message='{}'", message);
    }
}
