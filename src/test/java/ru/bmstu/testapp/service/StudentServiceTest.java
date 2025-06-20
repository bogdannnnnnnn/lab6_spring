package ru.bmstu.testapp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import ru.bmstu.testapp.aspect.UserContext;
import ru.bmstu.testapp.dao.StudentDao;
import ru.bmstu.testapp.model.Role;
import ru.bmstu.testapp.model.Student;
import ru.bmstu.testapp.service.impl.StudentServiceImpl;

class StudentServiceTest {

    @Mock
    private StudentDao studentDao;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        UserContext.clear();
    }

    @Test
    void getAllStudents_ShouldReturnStudentList() {
        // Given
        List<Student> expectedStudents = Arrays.asList(
                new Student("Ivanov", "Ivan", 5),
                new Student("Petrov", "Petr", 3)
        );
        when(studentDao.getAll()).thenReturn(expectedStudents);

        // When
        List<Student> actualStudents = studentService.getAllStudents();

        // Then
        assertEquals(expectedStudents, actualStudents);
        verify(studentDao).getAll();
    }

    @Test
    void getStudent_ExistingStudent_ShouldReturnStudent() {
        // Given
        Student expectedStudent = new Student("Ivanov", "Ivan", 5);
        when(studentDao.findByName("Ivanov", "Ivan")).thenReturn(Optional.of(expectedStudent));

        // When
        Optional<Student> actualStudent = studentService.getStudent("Ivanov", "Ivan");

        // Then
        assertTrue(actualStudent.isPresent());
        assertEquals(expectedStudent, actualStudent.get());
        verify(studentDao).findByName("Ivanov", "Ivan");
    }

    @Test
    void getStudent_NonExistingStudent_ShouldReturnEmpty() {
        // Given
        when(studentDao.findByName("Unknown", "Student")).thenReturn(Optional.empty());

        // When
        Optional<Student> actualStudent = studentService.getStudent("Unknown", "Student");

        // Then
        assertFalse(actualStudent.isPresent());
        verify(studentDao).findByName("Unknown", "Student");
    }

    @Test
    void changeTokens_ValidRequest_ShouldUpdateTokens() {
        // Given
        UserContext.setRole(Role.TEACHER);
        Student student = new Student("Ivanov", "Ivan", 5);
        when(studentDao.findByName("Ivanov", "Ivan")).thenReturn(Optional.of(student));

        // When
        studentService.changeTokens("Ivanov", "Ivan", 3);

        // Then
        assertEquals(8, student.getTokens());
        verify(auditService).record(anyString());
        verify(studentDao).saveToFile();
    }

    @Test
    void changeTokens_StudentNotFound_ShouldThrowException() {
        // Given
        UserContext.setRole(Role.TEACHER);
        when(studentDao.findByName("Unknown", "Student")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, 
                () -> studentService.changeTokens("Unknown", "Student", 3));
    }

    @Test
    void changeTokens_NegativeResult_ShouldThrowException() {
        // Given
        UserContext.setRole(Role.TEACHER);
        Student student = new Student("Ivanov", "Ivan", 5);
        when(studentDao.findByName("Ivanov", "Ivan")).thenReturn(Optional.of(student));

        // When & Then
        assertThrows(IllegalArgumentException.class, 
                () -> studentService.changeTokens("Ivanov", "Ivan", -10));
    }

    @Test
    void addStudent_ValidRequest_ShouldAddStudent() {
        // Given
        UserContext.setRole(Role.TEACHER);
        when(studentDao.findByName("New", "Student")).thenReturn(Optional.empty());
        List<Student> studentList = new ArrayList<>(Arrays.asList(new Student("Ivanov", "Ivan", 5)));
        when(studentDao.getAll()).thenReturn(studentList);

        // When
        studentService.addStudent("New", "Student");

        // Then
        verify(auditService).record(anyString());
        verify(studentDao).saveToFile();
    }

    @Test
    void addStudent_ExistingStudent_ShouldThrowException() {
        // Given
        UserContext.setRole(Role.TEACHER);
        when(studentDao.findByName("Ivanov", "Ivan")).thenReturn(Optional.of(new Student("Ivanov", "Ivan", 5)));

        // When & Then
        assertThrows(IllegalArgumentException.class, 
                () -> studentService.addStudent("Ivanov", "Ivan"));
    }

    @Test
    void removeStudent_ExistingStudent_ShouldRemoveStudent() {
        // Given
        UserContext.setRole(Role.TEACHER);
        Student student = new Student("Ivanov", "Ivan", 5);
        when(studentDao.findByName("Ivanov", "Ivan")).thenReturn(Optional.of(student));
        List<Student> studentList = new ArrayList<>(Arrays.asList(student));
        when(studentDao.getAll()).thenReturn(studentList);

        // When
        studentService.removeStudent("Ivanov", "Ivan");

        // Then
        verify(auditService).record(anyString());
        verify(studentDao).saveToFile();
    }

    @Test
    void removeStudent_NonExistingStudent_ShouldThrowException() {
        // Given
        UserContext.setRole(Role.TEACHER);
        when(studentDao.findByName("Unknown", "Student")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, 
                () -> studentService.removeStudent("Unknown", "Student"));
    }
} 