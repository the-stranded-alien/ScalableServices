package in.hotel.notification_service.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;

@Data
@Document(indexName = "audit")
public class AuditItem {
    @Id
    private String id;
    private String type;
    private String message;
    private String userId;
    private LocalDateTime timestamp;
    private String serviceSource;
}
