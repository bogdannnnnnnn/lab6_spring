package ru.bmstu.testapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.bmstu.testapp.model.Student;
import ru.bmstu.testapp.service.StudentService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    void getStatus_ShouldReturnOK() throws Exception {
        mockMvc.perform(get("/api/v1/getStatus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Application is running"));
    }

    @Test
    void getAllStudents_ShouldReturnStudentList() throws Exception {
        // Given
        List<Student> students = Arrays.asList(
                new Student("Ivanov", "Ivan", 5),
                new Student("Petrov", "Petr", 3)
        );
        when(studentService.getAllStudents()).thenReturn(students);

        // When & Then
        mockMvc.perform(get("/api/v1/students")
                        .param("role", "teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Ivanov"))
                .andExpect(jsonPath("$[0].firstName").value("Ivan"))
                .andExpect(jsonPath("$[0].tokens").value(5))
                .andExpect(jsonPath("$[1].lastName").value("Petrov"));

        verify(studentService).getAllStudents();
    }

    @Test
    void getStudent_ExistingStudent_ShouldReturnStudent() throws Exception {
        // Given
        Student student = new Student("Ivanov", "Ivan", 5);
        when(studentService.getStudent("Ivanov", "Ivan")).thenReturn(Optional.of(student));

        // When & Then
        mockMvc.perform(get("/api/v1/students/Ivanov/Ivan")
                        .param("role", "student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Ivanov"))
                .andExpect(jsonPath("$.firstName").value("Ivan"))
                .andExpect(jsonPath("$.tokens").value(5));

        verify(studentService).getStudent("Ivanov", "Ivan");
    }

    @Test
    void getStudent_NonExistingStudent_ShouldReturnNotFound() throws Exception {
        // Given
        when(studentService.getStudent("Unknown", "Student")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/students/Unknown/Student")
                        .param("role", "student"))
                .andExpect(status().isNotFound());

        verify(studentService).getStudent("Unknown", "Student");
    }

    @Test
    void addStudent_AsTeacher_ShouldSucceed() throws Exception {
        // Given
        doNothing().when(studentService).addStudent("New", "Student");

        // When & Then
        mockMvc.perform(post("/api/v1/students")
                        .param("role", "teacher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"lastName\":\"New\",\"firstName\":\"Student\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Student added successfully"));

        verify(studentService).addStudent("New", "Student");
    }

    @Test
    void addStudent_AsStudent_ShouldReturnForbidden() throws Exception {
        // Given
        doThrow(new SecurityException("Access denied for role: STUDENT"))
                .when(studentService).addStudent("New", "Student");

        // When & Then
        mockMvc.perform(post("/api/v1/students")
                        .param("role", "student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"lastName\":\"New\",\"firstName\":\"Student\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Access denied for role: STUDENT"));
    }

    @Test
    void changeTokens_AsTeacher_ShouldSucceed() throws Exception {
        // Given
        doNothing().when(studentService).changeTokens("Ivanov", "Ivan", 5);

        // When & Then
        mockMvc.perform(put("/api/v1/students/Ivanov/Ivan/tokens")
                        .param("role", "teacher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"delta\":5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tokens updated successfully"));

        verify(studentService).changeTokens("Ivanov", "Ivan", 5);
    }

    @Test
    void removeStudent_AsTeacher_ShouldSucceed() throws Exception {
        // Given
        doNothing().when(studentService).removeStudent("Ivanov", "Ivan");

        // When & Then
        mockMvc.perform(delete("/api/v1/students/Ivanov/Ivan")
                        .param("role", "teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Student removed successfully"));

        verify(studentService).removeStudent("Ivanov", "Ivan");
    }

    @Test
    void removeStudent_StudentNotFound_ShouldReturnBadRequest() throws Exception {
        // Given
        doThrow(new IllegalArgumentException("Student not found"))
                .when(studentService).removeStudent("Unknown", "Student");

        // When & Then
        mockMvc.perform(delete("/api/v1/students/Unknown/Student")
                        .param("role", "teacher"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Student not found"));
    }
} 