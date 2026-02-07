package com.agshin.extapp.model.enums;

public enum AuditType {
    USER_READ("USER_READ"),
    USER_CREATE("USER_CREATE"),
    USER_UPDATE("USER_UPDATE");


    private final String auditType;

    AuditType(String auditType) {
        this.auditType = auditType;
    }

}
