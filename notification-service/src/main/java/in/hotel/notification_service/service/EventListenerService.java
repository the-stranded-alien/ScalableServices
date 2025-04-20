package in.hotel.notification_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.hotel.common_library.models.AuditEvent;
import in.hotel.notification_service.component.AuditIndexNameProvider;
import in.hotel.notification_service.model.AuditItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class EventListenerService {

    @Autowired
    private AuditIndexNameProvider indexNameProvider;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = "hotel_notifications")
    public void handleHotelNotification(String message) {
        log.info("Received hotel notification message='{}'", message);
    }

    @RabbitListener(queues = "user_notifications")
    public void handleUserNotification(String message) {
        log.info("Received user notification message='{}'", message);
    }

    @RabbitListener(queues = "booking_notifications")
    public void handleBookingNotification(String message) {
        log.info("Received booking notification message='{}'", message);
    }

    @RabbitListener(queues = {"hotel_audit", "user_audit", "booking_audit"})
    public void handleAudit(String message, String queue) {
        try {
            log.info("Received audit message: {}", message);
            // No longer need to set queue in indexNameProvider since we're using a static index name

            AuditEvent auditEvent;
            try {
                auditEvent = objectMapper.readValue(message, AuditEvent.class);
            } catch (Exception e) {
                log.error("Failed to deserialize audit message: {}", e.getMessage(), e);
                return;
            }

            AuditItem auditItem = new AuditItem();
            auditItem.setId(UUID.randomUUID().toString());
            auditItem.setType(auditEvent.getType());
            auditItem.setMessage(auditEvent.getMessage());
            auditItem.setServiceSource(auditEvent.getServiceSource());
            auditItem.setUserId(auditEvent.getUserId());
            auditItem.setTimestamp(auditEvent.getTimestamp() != null ? auditEvent.getTimestamp() : LocalDateTime.now());

            elasticsearchOperations.save(auditItem);
            log.info("Saved audit item: {}", auditItem);
        } catch (Exception e) {
            log.error("Error handling audit message: {}", e.getMessage(), e);
        }
    }
}
