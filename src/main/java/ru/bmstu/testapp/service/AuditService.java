package ru.bmstu.testapp.service;

public interface AuditService {
    /**
     * Записать сообщение в журнал изменений (аудит).
     * @param message текст записи
     */
    void record(String message);
} 