package ru.bmstu.testapp.dao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import ru.bmstu.testapp.model.Student;

@Repository
public class StudentDaoCsvImpl implements StudentDao {

    @Value("${students.csv.path}")
    private String csvPath;

    // Вся информация хранящаяся в памяти
    private final List<Student> students = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            // Читаем CSV как ресурс из classpath
            ClassPathResource resource = new ClassPathResource(csvPath);
            
            try (InputStream inputStream = resource.getInputStream();
                 BufferedReader reader = new BufferedReader(
                     new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                
                // Первая строка — заголовок, пропускаем
                String line = reader.readLine();
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split(",");
                    if (parts.length < 3) continue;
                    String lastName = parts[0].trim();
                    String firstName = parts[1].trim();
                    int tokens = Integer.parseInt(parts[2].trim());
                    students.add(new Student(lastName, firstName, tokens));
                }
            }
        } catch (Exception e) {
            // Если файл не найден, работаем с пустым списком
            System.err.println("Warning: Failed to load students.csv: " + e.getMessage());
        }
    }

    @Override
    public List<Student> getAll() {
        return students;
    }

    @Override
    public Optional<Student> findByName(String lastName, String firstName) {
        return students.stream()
                .filter(s -> s.getLastName().equalsIgnoreCase(lastName)
                        && s.getFirstName().equalsIgnoreCase(firstName))
                .findFirst();
    }

    @Override
    public void saveToFile() {
        // Поскольку CSV читается как ресурс из classpath,
        // мы не можем сохранять изменения обратно в ресурс
        // Изменения сохраняются только в памяти приложения
        System.out.println("Note: Changes are saved in memory only. " +
                "Resource files in classpath cannot be modified at runtime.");
    }
} 