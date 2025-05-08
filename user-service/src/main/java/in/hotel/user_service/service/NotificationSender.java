package in.hotel.user_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.hotel.common_library.models.AuditEvent;
import in.hotel.common_library.models.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationSender  {

    private final RabbitTemplate rabbitTemplate;
    private final String USER_NOTIFICATION_QUEUE = "user_notifications";
    private final String USER_AUDIT_QUEUE = "user_audit";
    private final String NOTIFICATION_EXCHANGE = "notification_exchange";
    private final String AUDIT_EXCHANGE = "audit_exchange";
    private final ObjectMapper objectMapper;

    public void sendNotification(NotificationEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            log.info("Sending message to exchange: {}, routing key: {}, message: {}", NOTIFICATION_EXCHANGE, USER_NOTIFICATION_QUEUE, message);
            rabbitTemplate.convertAndSend(NOTIFICATION_EXCHANGE, USER_NOTIFICATION_QUEUE, message);
            log.info("Notification sent successfully");
        } catch (Exception e) {
            log.error("Failed to send message to RabbitMQ: {}", e.getMessage(), e);
        }
    }

    public void sendAudit(AuditEvent event){
        try {
            String message = objectMapper.writeValueAsString(event);
            log.info("Sending message to exchange: {}, routing key: {}, message: {}", AUDIT_EXCHANGE, USER_AUDIT_QUEUE, message);
            rabbitTemplate.convertAndSend(AUDIT_EXCHANGE, USER_AUDIT_QUEUE, message);
            log.info("Audit sent successfully");
        } catch (Exception e) {
            log.error("Failed to send message to RabbitMQ: {}", e.getMessage(), e);
        }
    }
}