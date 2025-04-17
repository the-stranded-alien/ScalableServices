package in.hotel.hotel_service.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationSender {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public NotificationSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}
