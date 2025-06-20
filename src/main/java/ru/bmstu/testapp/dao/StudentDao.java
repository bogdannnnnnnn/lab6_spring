package ru.bmstu.testapp.dao;

import java.util.List;
import java.util.Optional;

import ru.bmstu.testapp.model.Student;

public interface StudentDao {
    /**
     * При старте приложения читает весь CSV в память.
     * @return список всех студентов
     */
    List<Student> getAll();

    /**
     * Находит студента по полному имени (фамилия + имя).
     * @param lastName фамилия
     * @param firstName имя
     * @return Optional со Student, или пустой, если не найден
     */
    Optional<Student> findByName(String lastName, String firstName);

    /**
     * Сохраняет текущие данные обратно в CSV файл.
     */
    void saveToFile();
} 