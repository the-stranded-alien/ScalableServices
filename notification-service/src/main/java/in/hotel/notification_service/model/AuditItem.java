package in.hotel.notification_service.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;

@Data
@Document(indexName = "audit")
public class AuditItem {
    @Id
    private String id;
    private String type;
    private String message;
    private String userId;
    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private Instant timestamp;
    private String serviceSource;
}
