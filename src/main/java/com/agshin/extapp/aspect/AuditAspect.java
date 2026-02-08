package com.agshin.extapp.aspect;

import com.agshin.extapp.aspect.annotations.Auditable;
import com.agshin.extapp.services.AuditLogger;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
public class AuditAspect {
    private final AuditLogger auditLogger;
    private final HttpServletRequest request;

    public AuditAspect(AuditLogger auditLogger, HttpServletRequest request) {
        this.auditLogger = auditLogger;
        this.request = request;
    }


    @Around("@annotation(auditable)")
    public Object audit(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        Object[] args = joinPoint.getArgs();

        // Object before = args.length > 0 && args[0] != null ? args[0] : "anonymous";
        // can omit checks since email can never be null
        Object before = args[0];


        Object result = joinPoint.proceed();

        auditLogger.log(
                auditable.action().name(),
                auditable.entity(),
                extractId(result),
                before,
                result,
                request.getUserPrincipal() != null
                        ? request.getUserPrincipal().getName() // email
                        : "anonymous/thread",
                request.getRemoteAddr()
        );

        return result;
    }

    private String extractId(Object obj) {
        try {
            return Objects.requireNonNull(BeanUtils.getPropertyDescriptor(obj.getClass(), "id"))
                    .getReadMethod() // getter
                    .invoke(obj)// call method
                    .toString();
        } catch (Exception e) {
            return null;
        }
    }
}
