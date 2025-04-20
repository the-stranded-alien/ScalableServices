package in.hotel.notification_service.controller;

import in.hotel.notification_service.model.AuditItem;
import in.hotel.notification_service.service.AuditService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audit")
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
}
