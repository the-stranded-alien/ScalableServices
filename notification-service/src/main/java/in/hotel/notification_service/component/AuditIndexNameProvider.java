package in.hotel.notification_service.component;

import org.springframework.stereotype.Component;

@Component("auditIndexNameProvider")
public class AuditIndexNameProvider {

    private static final ThreadLocal<String> currentAuditQueue = new ThreadLocal<>();

    public void setQueue(String queueName) {
        currentAuditQueue.set(queueName.replace("-", "_"));
    }

    public String getIndexName() {
        String queue = currentAuditQueue.get();
        return queue != null ? "audit_" + queue : "audit_queue";
    }

    // This method is used to sanitize the index name to ensure it doesn't contain invalid characters
    public String getSanitizedIndexName(Object object) {
        String indexName = getIndexName();
        // If object is passed, we're in a situation where the object might be incorrectly used as index name
        // Return the proper index name instead
        return indexName;
    }

    public void clear() {
        currentAuditQueue.remove();
    }
}
