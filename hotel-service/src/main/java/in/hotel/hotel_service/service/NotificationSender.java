package in.hotel.hotel_service.service;

/**
 * Interface for sending notifications to external services.
 */
public interface NotificationSender {
    
    /**
     * Sends a notification message to the specified queue.
     *
     * @param queueName the name of the queue to send the notification to
     * @param message the message to send
     * @throws Exception if there is an error sending the notification
     */
    void sendNotification(String queueName, String message) throws Exception;
}