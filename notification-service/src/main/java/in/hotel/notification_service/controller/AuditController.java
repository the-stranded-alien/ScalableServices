package in.hotel.notification_service.controller;

import in.hotel.notification_service.model.AuditItem;
import in.hotel.notification_service.service.AuditService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    public List<AuditItem> getAll() {
        return auditService.getAllAuditEvents();
    }

    @GetMapping("/{service}")
    public List<AuditItem> getByService(@PathVariable String service) {
        return auditService.searchAuditEventsByService(service);
    }

    @GetMapping("/user/{userId}")
    public List<AuditItem> getByUser(@PathVariable String userId) {
        return auditService.searchAuditEventsByUserId(userId);
    }

    @GetMapping("/type/{eventType}")
    public List<AuditItem> getByEventType(@PathVariable String eventType) {
        return auditService.searchAuditEventsByEventType(eventType);
    }

    @GetMapping("/search")
    public List<AuditItem> searchByQuery(
            @RequestParam(required = false) String service,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String fromTimestamp,
            @RequestParam(required = false) String toTimestamp,
            @RequestParam(required = false) String keyword
    ) {
        return auditService.advancedSearch(service, userId, eventType, fromTimestamp, toTimestamp, keyword);
    }
}
