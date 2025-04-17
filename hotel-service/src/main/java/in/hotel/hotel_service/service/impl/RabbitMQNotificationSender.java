package in.hotel.hotel_service.service.impl;

import in.hotel.hotel_service.service.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQNotificationSender implements NotificationSender {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendNotification(String queueName, String message) throws Exception {
        try {
            // Determine the exchange based on the queue name
            String exchange = queueName.endsWith("-notifications") 
                ? "notification-exchange" 
                : "audit-exchange";
            
            // Use the queue name as the routing key
            String routingKey = queueName;
            
            log.info("Sending message to exchange: {}, routing key: {}, message: {}", 
                    exchange, routingKey, message);
            
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            
            log.info("Message sent successfully");
        } catch (Exception e) {
            log.error("Failed to send message to RabbitMQ: {}", e.getMessage(), e);
            throw new Exception("Failed to send notification: " + e.getMessage(), e);
        }
    }
}