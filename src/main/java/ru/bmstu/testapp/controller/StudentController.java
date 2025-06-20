package ru.bmstu.testapp.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.bmstu.testapp.aspect.UserContext;
import ru.bmstu.testapp.dto.StudentRequest;
import ru.bmstu.testapp.dto.TokenChangeRequest;
import ru.bmstu.testapp.model.Role;
import ru.bmstu.testapp.model.Student;
import ru.bmstu.testapp.service.StudentService;

@RestController
@RequestMapping("/api/v1")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/getStatus")
    public ResponseEntity<Map<String, String>> getStatus() {
        return ResponseEntity.ok(Map.of("status", "OK", "message", "Application is running"));
    }

    /**
     * Получить всех студентов
     */
    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents(@RequestParam(required = false) String role) {
        try {
            setUserRole(role);
            List<Student> students = studentService.getAllStudents();
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } finally {
            UserContext.clear();
        }
    }

    /**
     * Получить конкретного студента
     */
    @GetMapping("/students/{lastName}/{firstName}")
    public ResponseEntity<Student> getStudent(
            @PathVariable String lastName,
            @PathVariable String firstName,
            @RequestParam(required = false) String role) {
        try {
            setUserRole(role);
            Optional<Student> student = studentService.getStudent(lastName, firstName);
            if (student.isPresent()) {
                return ResponseEntity.ok(student.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } finally {
            UserContext.clear();
        }
    }

    /**
     * Добавить нового студента (только для преподавателя)
     */
    @PostMapping("/students")
    public ResponseEntity<Map<String, String>> addStudent(
            @RequestBody StudentRequest request,
            @RequestParam(required = false) String role) {
        try {
            setUserRole(role);
            studentService.addStudent(request.getLastName(), request.getFirstName());
            return ResponseEntity.ok(Map.of("message", "Student added successfully"));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        } finally {
            UserContext.clear();
        }
    }

    /**
     * Изменить количество токенов (только для преподавателя)
     */
    @PutMapping("/students/{lastName}/{firstName}/tokens")
    public ResponseEntity<Map<String, String>> changeTokens(
            @PathVariable String lastName,
            @PathVariable String firstName,
            @RequestBody TokenChangeRequest request,
            @RequestParam(required = false) String role) {
        try {
            setUserRole(role);
            studentService.changeTokens(lastName, firstName, request.getDelta());
            return ResponseEntity.ok(Map.of("message", "Tokens updated successfully"));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        } finally {
            UserContext.clear();
        }
    }

    /**
     * Удалить студента (только для преподавателя)
     */
    @DeleteMapping("/students/{lastName}/{firstName}")
    public ResponseEntity<Map<String, String>> removeStudent(
            @PathVariable String lastName,
            @PathVariable String firstName,
            @RequestParam(required = false) String role) {
        try {
            setUserRole(role);
            studentService.removeStudent(lastName, firstName);
            return ResponseEntity.ok(Map.of("message", "Student removed successfully"));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        } finally {
            UserContext.clear();
        }
    }

    private void setUserRole(String role) {
        if (role != null) {
            if ("teacher".equalsIgnoreCase(role)) {
                UserContext.setRole(Role.TEACHER);
            } else if ("student".equalsIgnoreCase(role)) {
                UserContext.setRole(Role.STUDENT);
            } else {
                throw new IllegalArgumentException("Invalid role: " + role);
            }
        } else {
            // По умолчанию студент, если роль не указана
            UserContext.setRole(Role.STUDENT);
        }
    }
} 