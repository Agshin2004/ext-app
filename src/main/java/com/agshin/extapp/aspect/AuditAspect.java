package com.agshin.extapp.aspect;

import com.agshin.extapp.aspect.annotations.Auditable;
import com.agshin.extapp.services.AuditLogger;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditAspect {
    @Autowired
    private AuditLogger auditLogger;

    @Autowired
    private HttpServletRequest request;

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
            return BeanUtils.getPropertyDescriptor(obj.getClass(), "id")
                    .getReadMethod()
                    .invoke(obj)
                    .toString();
        } catch (Exception e) {
            return null;
        }
    }
}
