package in.hotel.notification_service.service;

import in.hotel.common_library.models.AuditEvent;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final ElasticsearchClient elasticsearchClient;

    public void storeAuditEvent(AuditEvent auditEvent) {

    }
}
