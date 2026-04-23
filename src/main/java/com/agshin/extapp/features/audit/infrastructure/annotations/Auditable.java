package com.agshin.extapp.features.audit.infrastructure.annotations;


import com.agshin.extapp.features.audit.domain.AuditType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {
    AuditType action();
    String entity();
}
