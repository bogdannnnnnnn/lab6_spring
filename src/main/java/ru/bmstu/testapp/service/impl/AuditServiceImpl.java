package ru.bmstu.testapp.service.impl;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ru.bmstu.testapp.service.AuditService;

@Service
public class AuditServiceImpl implements AuditService {

    @Value("${audit.log.path}")
    private String auditLogPath;

    private PrintWriter writer;

    @PostConstruct
    public void init() {
        try {
            writer = new PrintWriter(new FileWriter(auditLogPath, true));
        } catch (Exception e) {
            throw new RuntimeException("Cannot open audit log file: " + auditLogPath, e);
        }
    }

    @Override
    public void record(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        writer.println("[" + timestamp + "] " + message);
        writer.flush();
    }
} 