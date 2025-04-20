package in.hotel.notification_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.hotel.common_library.models.AuditEvent;
import in.hotel.notification_service.component.AuditIndexNameProvider;
import in.hotel.notification_service.config.TestElasticsearchConfig;
import in.hotel.notification_service.model.AuditItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Import(TestElasticsearchConfig.class)
class NotificationServiceApplicationTests {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private AuditIndexNameProvider auditIndexNameProvider;

	@Test
	void contextLoads() {
	}

	@Test
	void testAuditEventDeserialization() throws Exception {
		// Sample JSON that matches the structure in the error log
		String json = "{\"id\":null,\"type\":\"HOTELS_CREATED\",\"message\":\"New hotel created with Id: 6804fe7828b37908d2ffdb8f, Name: string by User\",\"userId\":null,\"timestamp\":\"2025-04-20T14:02:32.17671709\",\"serviceSource\":\"Hotel Service\"}";

		// Deserialize JSON to AuditEvent
		AuditEvent auditEvent = objectMapper.readValue(json, AuditEvent.class);

		// Verify the object was properly deserialized
		assertNotNull(auditEvent);
		assertEquals("HOTELS_CREATED", auditEvent.getType());
		assertEquals("New hotel created with Id: 6804fe7828b37908d2ffdb8f, Name: string by User", auditEvent.getMessage());
		assertEquals("Hotel Service", auditEvent.getServiceSource());
	}

	@Test
	void testAuditIndexNameGeneration() {
		// Set a queue name
		auditIndexNameProvider.setQueue("test_queue");

		// Create a sample AuditItem with special characters
		AuditItem auditItem = new AuditItem();
		auditItem.setId("test-id");
		auditItem.setType("TEST_TYPE");
		auditItem.setMessage("Test message with special characters: \"'\\/ ,|>?*<");
		auditItem.setUserId("test-user");
		auditItem.setTimestamp(LocalDateTime.now());
		auditItem.setServiceSource("Test Service");

		// Get the sanitized index name from the provider
		String indexName = auditIndexNameProvider.getSanitizedIndexName(auditItem);

		// Verify the index name from the provider is valid
		assertNotNull(indexName);
		assertEquals("audit_test_queue", indexName);

		// Verify the static index name in the AuditItem class
		assertEquals("audit", auditItem.getClass().getAnnotation(org.springframework.data.elasticsearch.annotations.Document.class).indexName());

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

		// Clean up
		auditIndexNameProvider.clear();
	}
}
