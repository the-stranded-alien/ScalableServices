package in.hotel.common_library.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuditEventTest {

    @Test
    void testDeserialization() throws Exception {
        // Create ObjectMapper with JavaTimeModule for handling LocalDateTime
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Sample JSON that matches the structure in the error log
        String json = "{\"id\":null,\"type\":\"HOTELS_CREATED\",\"message\":\"New hotel created with Id: 6804fe7828b37908d2ffdb8f, Name: string by User\",\"userId\":null,\"timestamp\":\"2025-04-20T14:02:32.17671709\",\"serviceSource\":\"Hotel Service\"}";

        // Deserialize JSON to AuditEvent
        AuditEvent auditEvent = objectMapper.readValue(json, AuditEvent.class);

        // Verify the object was properly deserialized
        assertNotNull(auditEvent);
        assertEquals("HOTELS_CREATED", auditEvent.getType());
        assertEquals("New hotel created with Id: 6804fe7828b37908d2ffdb8f, Name: string by User", auditEvent.getMessage());
        assertEquals("Hotel Service", auditEvent.getServiceSource());

        System.out.println("[DEBUG_LOG] Successfully deserialized AuditEvent: " + auditEvent);
    }
}
