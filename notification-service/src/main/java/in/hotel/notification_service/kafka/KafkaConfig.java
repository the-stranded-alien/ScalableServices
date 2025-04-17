package in.hotel.notification_service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.topic.partitions:3}")
    private int topicPartitions;

    private static final short REPLICATION_FACTOR = 1;

    private static final List<String> REQUIRED_TOPICS = Arrays.asList(
            "hotel-notifications",
            "user-notifications",
            "booking-notifications",
            "hotel-audit",
            "user-audit",
            "booking-audit"
    );

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(kafkaAdmin().getConfigurationProperties());
    }

    @PostConstruct
    public void createTopics() {
        try {
            List<NewTopic> newTopics;
            try (AdminClient adminClient = adminClient()) {

                Set<String> existingTopics = new HashSet<>(adminClient.listTopics().names().get());

                List<String> topicsToCreate = REQUIRED_TOPICS.stream()
                        .filter(topic -> !existingTopics.contains(topic))
                        .toList();

                if (topicsToCreate.isEmpty()) {
                    log.info("All required Kafka topics already exist");
                    return;
                }

                newTopics = topicsToCreate.stream()
                        .map(topicName -> new NewTopic(topicName, topicPartitions, REPLICATION_FACTOR))
                        .collect(Collectors.toList());

                adminClient.createTopics(newTopics);
            }

            newTopics.forEach(topic ->
                log.info("Created Kafka topic: {} with {} partitions", 
                        topic.name(), topic.numPartitions()));
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error checking or creating Kafka topics: {}", e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("Error creating Kafka topics: {}", e.getMessage());
        }
    }
}
