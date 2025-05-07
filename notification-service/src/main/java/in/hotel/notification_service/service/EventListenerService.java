package in.hotel.notification_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.hotel.common_library.models.AuditEvent;
import in.hotel.common_library.models.NotificationEvent;
import in.hotel.notification_service.model.AuditItem;
import in.hotel.notification_service.model.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
@Slf4j
public class EventListenerService {

    private static final String ADMIN_EMAIL = "sahil16gupta11@gmail.com";

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmailService emailService;


    @RabbitListener(queues = {"hotel_notifications", "user_notifications", "booking_notifications"})
    public void handleNotification(String message, String queue) {
        try {
            log.info("Received notification message: {}", message);
            NotificationEvent notificationEvent;
            try {
                notificationEvent = objectMapper.readValue(message, NotificationEvent.class);
            } catch (Exception e) {
                log.error("Failed to deserialize notification message: {}", e.getMessage(), e);
                return;
            }
            Email email = new Email();
            String emailTo = notificationEvent.getUserEmail() != null ? notificationEvent.getUserEmail() : ADMIN_EMAIL;
            email.setTo(emailTo);
            email.setSubject(notificationEvent.getType());
            email.setBody(notificationEvent.getMessage());
            emailService.sendEmail(email);
            log.info("Sent email to {} with subject {}", email.getTo(), email.getSubject());
        } catch (Exception e) {
            log.error("Error handling notification message: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = {"hotel_audit", "user_audit", "booking_audit"})
    public void handleAudit(String message, String queue) {
        try {
            log.info("Received audit message: {}", message);

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
            auditItem.setUserId(auditEvent.getUserId());
            auditItem.setServiceSource(auditEvent.getServiceSource());
            auditItem.setUserId(auditEvent.getUserId());
            LocalDateTime timestamp = auditEvent.getTimestamp() != null
                    ? auditEvent.getTimestamp()
                    : LocalDateTime.now();

            Instant instant = timestamp.atZone(ZoneId.systemDefault()).toInstant();

            auditItem.setTimestamp(instant);

            elasticsearchOperations.save(auditItem);
            log.info("Saved audit item: {}", auditItem);
        } catch (Exception e) {
            log.error("Error handling audit message: {}", e.getMessage(), e);
        }
    }
}
