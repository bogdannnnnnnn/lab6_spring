package ru.bmstu.testapp.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import ru.bmstu.testapp.model.Student;

@Repository
public class StudentDaoCsvImpl implements StudentDao {

    @Value("${students.csv.path}")
    private Resource csvResource;

    // Вся информация хранящаяся в памяти
    private final List<Student> students = new ArrayList<>();

    @PostConstruct
    public void init() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(csvResource.getInputStream(), StandardCharsets.UTF_8))) {
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
        } catch (Exception e) {
            throw new RuntimeException("Failed to load students.csv", e);
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
        try {
            // Получаем путь к файлу в src/main/resources
            String resourcePath = csvResource.getURI().getPath();
            
            List<String> lines = new ArrayList<>();
            lines.add("lastName,firstName,tokens");
            
            for (Student student : students) {
                lines.add(String.format("%s,%s,%d", 
                    student.getLastName(), 
                    student.getFirstName(), 
                    student.getTokens()));
            }
            
            Files.write(Paths.get(resourcePath), lines, StandardCharsets.UTF_8, 
                       StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                       
        } catch (Exception e) {
            throw new RuntimeException("Failed to save students to CSV", e);
        }
    }
} 