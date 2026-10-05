package com.erp.audit.service;

import com.erp.audit.entity.AuditLog;
import com.erp.audit.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.List;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    @Transactional
    public AuditLog log(
            String username,
            String action,
            String module,
            String entityType,
            Long entityId,
            String description) {

        AuditLog auditLog = new AuditLog(
                username,
                action,
                module,
                entityType,
                entityId,
                description
        );

        return auditLogRepository.save(auditLog);
    }
    @Override
@Transactional(readOnly = true)
public Page<AuditLog> getLogsPaginated(
        int page,
        int size) {

    Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(
                    Sort.Direction.DESC,
                    "createdAt"
            )
    );

    return auditLogRepository
            .findAllByOrderByCreatedAtDesc(pageable);
}

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByUsername(String username) {
        return auditLogRepository.findByUsernameOrderByCreatedAtDesc(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByModule(String module) {
        return auditLogRepository.findByModuleOrderByCreatedAtDesc(module);
    }
}