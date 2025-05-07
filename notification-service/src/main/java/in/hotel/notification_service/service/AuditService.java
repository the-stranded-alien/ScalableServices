package in.hotel.notification_service.service;

import in.hotel.notification_service.model.AuditItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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

    public List<AuditItem> searchAuditEventsByUserId(String userId) {
        Criteria criteria = Criteria.where("userId").is(userId);
        CriteriaQuery query = new CriteriaQuery(criteria);
        return elasticsearchOperations.search(query, AuditItem.class)
                .map(SearchHit::getContent)
                .toList();
    }

    public List<AuditItem> searchAuditEventsByEventType(String eventType) {
        Criteria criteria = Criteria.where("eventType").is(eventType);
        CriteriaQuery query = new CriteriaQuery(criteria);
        return elasticsearchOperations.search(query, AuditItem.class)
                .map(SearchHit::getContent)
                .toList();
    }

    public List<AuditItem> advancedSearch(String service, String userId, String eventType, String from, String to, String keyword) {
        List<Criteria> criteriaList = new ArrayList<>();

        if (service != null) {
            criteriaList.add(Criteria.where("service").is(service));
        }

        if (userId != null) {
            criteriaList.add(Criteria.where("userId").is(userId));
        }

        if (eventType != null) {
            criteriaList.add(Criteria.where("eventType").is(eventType));
        }

        if (from != null || to != null) {
            try {
                Criteria rangeCriteria = Criteria.where("timestamp");
                if (from != null) rangeCriteria = rangeCriteria.greaterThanEqual(Instant.parse(from));
                if (to != null) rangeCriteria = rangeCriteria.lessThanEqual(Instant.parse(to));
                criteriaList.add(rangeCriteria);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Use ISO-8601 format (e.g., 2024-12-31T23:59:59Z)");
            }
        }

        if (keyword != null) {
            criteriaList.add(Criteria.where("message").matches(keyword).or("details").matches(keyword));
        }

        Criteria finalCriteria = new Criteria();
        if (!criteriaList.isEmpty()) {
            finalCriteria = criteriaList.get(0);
            for (int i = 1; i < criteriaList.size(); i++) {
                finalCriteria = finalCriteria.and(criteriaList.get(i));
            }
        }

        CriteriaQuery query = new CriteriaQuery(finalCriteria);
        return elasticsearchOperations.search(query, AuditItem.class)
                .map(SearchHit::getContent)
                .toList();
    }
}
