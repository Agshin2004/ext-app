package com.agshin.extapp.services;

import com.agshin.extapp.model.entities.AuditLog;
import com.agshin.extapp.repositories.AuditLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditLogger {

    @Autowired
    private AuditLogRepository repo;


    @Async
    public void log(
            String action,
            String entity,
            String entityId,
            Object beforeState,
            Object afterState,
            String user,
            String ip) {

        AuditLog log = new AuditLog();

        log.setAction(action);
        log.setEntity(entity);
        log.setEntityId(entityId);
        log.setPerformedBy(user);
        log.setBeforeState(ip);
        log.setBeforeState(toJson(beforeState));
        log.setAfterState(toJson(afterState));

        log.setCreatedAt(LocalDateTime.now());

        repo.save(log);

    }


    private String toJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception ex) {
            // todo: write custom exception
            return "unserializable";
        }
    }
}
