package in.hotel.hotel_service.constant;

public enum KafkaTopics {
    HOTEL_NOTIFICATION_TOPIC("hotel-notification"),
    HOTEL_AUDIT_TOPIC("hotel-audit");

    private final String topicName;
    KafkaTopics(String topicName) {
        this.topicName = topicName;
    }
    public String getTopicName() {
        return topicName;
    }
}
