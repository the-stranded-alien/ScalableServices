package in.hotel.notification_service.rabbit_mq;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;


@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange("notification-exchange");
    }

    @Bean
    public TopicExchange auditExchange() {
        return new TopicExchange("audit-exchange");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue hotelNotificationsQueue() {
        return new Queue("hotel-notifications", true);
    }

    @Bean
    public Queue userNotificationsQueue() {
        return new Queue("user-notifications", true);
    }

    @Bean
    public Queue bookingNotificationsQueue() {
        return new Queue("booking-notifications", true);
    }

    @Bean
    public Queue hotelAuditQueue() {
        return new Queue("hotel-audit", true);
    }

    @Bean
    public Queue userAuditQueue() {
        return new Queue("user-audit", true);
    }

    @Bean
    public Queue bookingAuditQueue() {
        return new Queue("booking-audit", true);
    }

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