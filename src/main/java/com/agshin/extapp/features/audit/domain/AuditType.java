package com.agshin.extapp.features.audit.domain;

public enum AuditType {
    USER_READ("USER_READ"),
    USER_CREATE("USER_CREATE"),
    USER_UPDATE("USER_UPDATE"),
    STATE_CHANGE("STATE_CHANGE");


    private final String auditType;

    AuditType(String auditType) {
        this.auditType = auditType;
    }

}
