package in.hotel.hotel_service.constant;

public enum RabbitMQQueues {
    HOTEL_NOTIFICATION_QUEUE("hotel-notifications"),
    HOTEL_AUDIT_QUEUE("hotel-audit");

    private final String queueName;
    RabbitMQQueues(String queueName) {
        this.queueName = queueName;
    }
    public String getQueueName() {
        return queueName;
    }
}
