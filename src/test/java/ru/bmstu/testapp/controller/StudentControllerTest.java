package ru.bmstu.testapp.controller;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ru.bmstu.testapp.model.Student;
import ru.bmstu.testapp.service.StudentService;

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
    void getStatus() throws Exception {
        mockMvc.perform(get("/api/v1/getStatus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"));
    }

    @Test
    void getAllStudents() throws Exception {
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(
                new Student("Ivanov", "Ivan", 5)
        ));

        mockMvc.perform(get("/api/v1/students").param("role", "teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Ivanov"));
    }

    @Test
    void getStudentOk() throws Exception {
        when(studentService.getStudent("Ivanov", "Ivan"))
                .thenReturn(Optional.of(new Student("Ivanov", "Ivan", 5)));

        mockMvc.perform(get("/api/v1/students/Ivanov/Ivan").param("role", "student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Ivanov"));
    }

    @Test
    void getStudentNotFound() throws Exception {
        when(studentService.getStudent("Unknown", "Student")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/students/Unknown/Student").param("role", "student"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addStudent() throws Exception {
        doNothing().when(studentService).addStudent("New", "Student");

        mockMvc.perform(post("/api/v1/students")
                        .param("role", "teacher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"lastName\":\"New\",\"firstName\":\"Student\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void changeTokens() throws Exception {
        doNothing().when(studentService).changeTokens("Ivanov", "Ivan", 5);

        mockMvc.perform(put("/api/v1/students/Ivanov/Ivan/tokens")
                        .param("role", "teacher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"delta\":5}"))
                .andExpect(status().isOk());
    }

    @Test
    void removeStudent() throws Exception {
        doNothing().when(studentService).removeStudent("Ivanov", "Ivan");

        mockMvc.perform(delete("/api/v1/students/Ivanov/Ivan").param("role", "teacher"))
                .andExpect(status().isOk());
    }
} 