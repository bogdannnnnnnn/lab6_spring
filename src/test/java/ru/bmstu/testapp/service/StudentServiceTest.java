package ru.bmstu.testapp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    void getAllStudents() {
        when(studentDao.getAll()).thenReturn(Arrays.asList(new Student("Ivanov", "Ivan", 5)));
        assertEquals(1, studentService.getAllStudents().size());
    }

    @Test
    void getStudentExists() {
        when(studentDao.findByName("Ivanov", "Ivan"))
                .thenReturn(Optional.of(new Student("Ivanov", "Ivan", 5)));
        assertTrue(studentService.getStudent("Ivanov", "Ivan").isPresent());
    }

    @Test
    void getStudentNotExists() {
        when(studentDao.findByName("Unknown", "Student")).thenReturn(Optional.empty());
        assertFalse(studentService.getStudent("Unknown", "Student").isPresent());
    }

    @Test
    void changeTokens() {
        UserContext.setRole(Role.TEACHER);
        Student student = new Student("Ivanov", "Ivan", 5);
        when(studentDao.findByName("Ivanov", "Ivan")).thenReturn(Optional.of(student));
        studentService.changeTokens("Ivanov", "Ivan", 3);
        assertEquals(8, student.getTokens());
        verify(studentDao).saveToFile();
    }

    @Test
    void changeTokensNotFound() {
        UserContext.setRole(Role.TEACHER);
        when(studentDao.findByName("Unknown", "Student")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, 
                () -> studentService.changeTokens("Unknown", "Student", 3));
    }

    @Test
    void addStudent() {
        UserContext.setRole(Role.TEACHER);
        when(studentDao.findByName("New", "Student")).thenReturn(Optional.empty());
        when(studentDao.getAll()).thenReturn(new ArrayList<>());
        studentService.addStudent("New", "Student");
        verify(studentDao).saveToFile();
    }

    @Test
    void addStudentExists() {
        UserContext.setRole(Role.TEACHER);
        when(studentDao.findByName("Ivanov", "Ivan"))
                .thenReturn(Optional.of(new Student("Ivanov", "Ivan", 5)));
        assertThrows(IllegalArgumentException.class, 
                () -> studentService.addStudent("Ivanov", "Ivan"));
    }

    @Test
    void removeStudent() {
        UserContext.setRole(Role.TEACHER);
        Student student = new Student("Ivanov", "Ivan", 5);
        when(studentDao.findByName("Ivanov", "Ivan")).thenReturn(Optional.of(student));
        when(studentDao.getAll()).thenReturn(new ArrayList<>(Arrays.asList(student)));
        studentService.removeStudent("Ivanov", "Ivan");
        verify(studentDao).saveToFile();
    }
} 