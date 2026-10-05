package com.erp.audit.service;

import com.erp.audit.entity.AuditLog;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AuditLogService {

    AuditLog log(
            String username,
            String action,
            String module,
            String entityType,
            Long entityId,
            String description
    );

    List<AuditLog> getAllLogs();

    List<AuditLog> getLogsByUsername(String username);

    List<AuditLog> getLogsByModule(String module);

    Page<AuditLog> getLogsPaginated(
            int page,
            int size
    );
}