package com.agshin.extapp.features.audit.infrastructure;

import com.agshin.extapp.features.audit.domain.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
