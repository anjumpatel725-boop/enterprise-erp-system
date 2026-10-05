package com.erp.audit.controller;

import com.erp.audit.entity.AuditLog;
import com.erp.audit.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }
@GetMapping("/paged")
public ResponseEntity<Page<AuditLog>> getLogsPaginated(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

    return ResponseEntity.ok(
            auditLogService.getLogsPaginated(page, size)
    );
}

    // Get all audit logs
    @GetMapping
    public ResponseEntity<List<AuditLog>> getAllLogs() {
        return ResponseEntity.ok(
                auditLogService.getAllLogs()
        );
    }

    // Get logs by username
    @GetMapping("/user/{username}")
    public ResponseEntity<List<AuditLog>> getLogsByUsername(
            @PathVariable String username) {

        return ResponseEntity.ok(
                auditLogService.getLogsByUsername(username)
        );
    }

    // Get logs by module
    @GetMapping("/module/{module}")
    public ResponseEntity<List<AuditLog>> getLogsByModule(
            @PathVariable String module) {

        return ResponseEntity.ok(
                auditLogService.getLogsByModule(module)
        );
    }
}