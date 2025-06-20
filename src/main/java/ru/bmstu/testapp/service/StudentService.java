package ru.bmstu.testapp.service;

import java.util.List;
import java.util.Optional;

import ru.bmstu.testapp.model.Student;

public interface StudentService {
    List<Student> getAllStudents();

    Optional<Student> getStudent(String lastName, String firstName);

    void changeTokens(String lastName, String firstName, int delta);

    void addStudent(String lastName, String firstName);

    void removeStudent(String lastName, String firstName);
} 