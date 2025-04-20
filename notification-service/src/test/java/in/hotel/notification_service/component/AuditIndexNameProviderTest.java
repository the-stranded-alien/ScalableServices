package in.hotel.notification_service.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuditIndexNameProviderTest {

    private AuditIndexNameProvider provider;

    @BeforeEach
    void setUp() {
        provider = new AuditIndexNameProvider();
        // Clear the ThreadLocal variable to ensure tests are isolated
        provider.clear();
    }

    @Test
    void testGetIndexName() {
        // Test with no queue set
        assertEquals("audit_queue", provider.getIndexName());

        // Test with queue set
        provider.setQueue("test_queue");
        assertEquals("audit_test_queue", provider.getIndexName());

        // Test with hyphen in queue name
        provider.setQueue("test-queue");
        assertEquals("audit_test_queue", provider.getIndexName());
    }

    @Test
    void testGetSanitizedIndexName() {
        // Set a queue name
        provider.setQueue("test_queue");

        // Test with null object
        assertEquals("audit_test_queue", provider.getSanitizedIndexName(null));

        // Test with string object
        assertEquals("audit_test_queue", provider.getSanitizedIndexName("some string"));

        // Test with object that might be used as index name
        Object testObject = new Object() {
            @Override
            public String toString() {
                return "{\"id\":null,\"type\":\"TEST\",\"message\":\"Test message\",\"userId\":null,\"timestamp\":\"2025-04-20T14:17:44.138079929\",\"serviceSource\":\"Test Service\"}";
            }
        };
        String indexName = provider.getSanitizedIndexName(testObject);
        assertEquals("audit_test_queue", indexName);

        // Verify the index name doesn't contain forbidden characters
        assertFalse(indexName.contains("\""));
        assertFalse(indexName.contains(" "));
        assertFalse(indexName.contains("\\"));
        assertFalse(indexName.contains("/"));
        assertFalse(indexName.contains(","));
        assertFalse(indexName.contains("|"));
        assertFalse(indexName.contains(">"));
        assertFalse(indexName.contains("?"));
        assertFalse(indexName.contains("*"));
        assertFalse(indexName.contains("<"));
    }

    @Test
    void testClear() {
        // Set a queue name
        provider.setQueue("test_queue");
        assertEquals("audit_test_queue", provider.getIndexName());

        // Clear the queue name
        provider.clear();
        assertEquals("audit_queue", provider.getIndexName());
    }
}
