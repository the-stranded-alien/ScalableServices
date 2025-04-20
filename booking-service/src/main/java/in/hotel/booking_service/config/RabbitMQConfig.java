package in.hotel.booking_service.config;

import in.hotel.booking_service.model.enums.Exchanges;
import in.hotel.booking_service.model.enums.Queues;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchanges
    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(Exchanges.NOTIFICATION_EXCHANGE.getName());
    }

    @Bean
    public TopicExchange auditExchange() {
        return new TopicExchange(Exchanges.AUDIT_EXCHANGE.getName());
    }

    // Message Converter
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Queues for Notification Events
    @Bean
    public Queue hotelNotificationsQueue() {
        return new Queue(Queues.HOTEL_NOTIFICATION.getName(), true);
    }

    @Bean
    public Queue userNotificationsQueue() {
        return new Queue(Queues.USER_NOTIFICATION.getName(), true);
    }

    @Bean
    public Queue bookingNotificationsQueue() {
        return new Queue(Queues.BOOKING_NOTIFICATION.getName(), true);
    }

    // Queues for Audit Events
    @Bean
    public Queue hotelAuditQueue() {
        return new Queue(Queues.HOTEL_AUDIT.getName(), true);
    }

    @Bean
    public Queue userAuditQueue() {
        return new Queue(Queues.USER_AUDIT.getName(), true);
    }

    @Bean
    public Queue bookingAuditQueue() {
        return new Queue(Queues.BOOKING_AUDIT.getName(), true);
    }

    // Bindings for Notification Queues
    @Bean
    public Binding hotelNotificationsBinding(Queue hotelNotificationsQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(hotelNotificationsQueue).to(notificationExchange).with(Queues.HOTEL_NOTIFICATION.getName());
    }

    @Bean
    public Binding userNotificationsBinding(Queue userNotificationsQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(userNotificationsQueue).to(notificationExchange).with(Queues.USER_NOTIFICATION.getName());
    }

    @Bean
    public Binding bookingNotificationsBinding(Queue bookingNotificationsQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(bookingNotificationsQueue).to(notificationExchange).with(Queues.BOOKING_NOTIFICATION.getName());
    }

    // Bindings for Audit Queues
    @Bean
    public Binding hotelAuditBinding(Queue hotelAuditQueue, TopicExchange auditExchange) {
        return BindingBuilder.bind(hotelAuditQueue).to(auditExchange).with(Queues.HOTEL_AUDIT.getName());
    }

    @Bean
    public Binding userAuditBinding(Queue userAuditQueue, TopicExchange auditExchange) {
        return BindingBuilder.bind(userAuditQueue).to(auditExchange).with(Queues.USER_AUDIT.getName());
    }

    @Bean
    public Binding bookingAuditBinding(Queue bookingAuditQueue, TopicExchange auditExchange) {
        return BindingBuilder.bind(bookingAuditQueue).to(auditExchange).with(Queues.BOOKING_AUDIT.getName());
    }

    // RabbitTemplate and RabbitAdmin
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}