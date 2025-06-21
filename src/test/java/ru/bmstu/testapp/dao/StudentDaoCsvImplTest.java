package ru.bmstu.testapp.dao;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;

import ru.bmstu.testapp.model.Student;

/**
 * Простая проверка чтения и записи CSV для DAO.
 */
class StudentDaoCsvImplTest {

    @TempDir
    Path tempDir;

    private Path csvFile;
    private StudentDaoCsvImpl dao;

    @BeforeEach
    void setUp() throws Exception {
        csvFile = tempDir.resolve("students.csv");
        // создаём файл с заголовком
        Files.write(csvFile, List.of("lastName,firstName,tokens"), StandardCharsets.UTF_8);

        dao = new StudentDaoCsvImpl();
        // подменяем путь через reflection
        ReflectionTestUtils.setField(dao, "csvPath", csvFile.toString());
        dao.init();
    }

    @Test
    void addAndSave() throws Exception {
        dao.getAll().add(new Student("Ivanov", "Ivan", 5));
        dao.saveToFile();

        List<String> lines = Files.readAllLines(csvFile);
        assertEquals(2, lines.size());
        assertEquals("Ivanov,Ivan,5", lines.get(1));
    }

} 