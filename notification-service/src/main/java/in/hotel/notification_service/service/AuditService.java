package in.hotel.notification_service.service;

import in.hotel.notification_service.model.AuditItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final ElasticsearchOperations elasticsearchOperations;

    public List<AuditItem> searchAuditEventsByService(String serviceName) {
        Criteria criteria = Criteria.where("service").is(serviceName);
        CriteriaQuery query = new CriteriaQuery(criteria);
        return elasticsearchOperations.search(query, AuditItem.class)
                .map(SearchHit::getContent)
                .toList();
    }

    public List<AuditItem> getAllAuditEvents() {
        return elasticsearchOperations.search(new CriteriaQuery(new Criteria()), AuditItem.class)
                .map(SearchHit::getContent)
                .toList();
    }
}
