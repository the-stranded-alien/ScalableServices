package in.hotel.notification_service.repository;

import in.hotel.notification_service.model.AuditItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends ElasticsearchRepository<AuditItem, String> {
}
