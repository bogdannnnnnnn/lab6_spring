package ru.bmstu.testapp.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.bmstu.testapp.aspect.RequireRole;
import ru.bmstu.testapp.aspect.RoleType;
import ru.bmstu.testapp.aspect.UserContext;
import ru.bmstu.testapp.dao.StudentDao;
import ru.bmstu.testapp.model.Role;
import ru.bmstu.testapp.model.Student;
import ru.bmstu.testapp.service.AuditService;
import ru.bmstu.testapp.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentDao studentDao;
    private final AuditService auditService;

    @Autowired
    public StudentServiceImpl(StudentDao studentDao, AuditService auditService) {
        this.studentDao = studentDao;
        this.auditService = auditService;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDao.getAll();
    }

    @Override
    public Optional<Student> getStudent(String lastName, String firstName) {
        return studentDao.findByName(lastName, firstName);
    }

    @Override
    @RequireRole(RoleType.TEACHER)
    public void changeTokens(String lastName, String firstName, int delta) {
        Role role = UserContext.getRole();
        Optional<Student> opt = studentDao.findByName(lastName, firstName);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("Student not found");
        }
        Student s = opt.get();

        int newTokens = s.getTokens() + delta;
        if (newTokens < 0) {
            throw new IllegalArgumentException("Resulting tokens cannot be negative");
        }
        s.setTokens(newTokens);

        String who = (role == Role.TEACHER ? "TEACHER" : "STUDENT");
        auditService.record(who + " changed tokens for " +
                lastName + " " + firstName + " by " + delta +
                ". New balance: " + newTokens);
        
        // Сохраняем изменения в CSV
        studentDao.saveToFile();
    }

    @Override
    @RequireRole(RoleType.TEACHER)
    public void addStudent(String lastName, String firstName) {
        Role role = UserContext.getRole();
        Optional<Student> existing = studentDao.findByName(lastName, firstName);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Student already exists");
        }
        Student newStudent = new Student(lastName, firstName, 0);
        studentDao.getAll().add(newStudent);
        auditService.record("TEACHER added new student " + lastName + " " + firstName);
        
        // Сохраняем изменения в CSV
        studentDao.saveToFile();
    }

    @Override
    @RequireRole(RoleType.TEACHER)
    public void removeStudent(String lastName, String firstName) {
        Role role = UserContext.getRole();
        Optional<Student> existing = studentDao.findByName(lastName, firstName);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Student not found");
        }
        studentDao.getAll().remove(existing.get());
        auditService.record("TEACHER removed student " + lastName + " " + firstName);
        
        // Сохраняем изменения в CSV
        studentDao.saveToFile();
    }
} 