package ru.bmstu.testapp;

import java.util.List;
import java.util.Scanner;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ru.bmstu.testapp.aspect.UserContext;
import ru.bmstu.testapp.model.Role;
import ru.bmstu.testapp.model.Student;
import ru.bmstu.testapp.service.StudentService;

/**
 * Тестовое консольное приложение для проверки работы бизнес-логики
 * без необходимости развертывания в Tomcat
 */
public class TestApplication {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "console");
        
        // Запускаем Spring-контекст с консольной конфигурацией
        AnnotationConfigApplicationContext context = null;
        try {
            System.out.println("Starting console application...");
            context = new AnnotationConfigApplicationContext(ru.bmstu.testapp.config.SimpleConsoleConfig.class);
            
            // Получаем бин StudentService
            StudentService studentService = context.getBean(StudentService.class);

            Scanner scanner = new Scanner(System.in);

            System.out.println("This is a console test for business logic validation");
            System.out.println();

            // Устанавливаем роль преподавателя для тестирования
            UserContext.setRole(Role.TEACHER);

            boolean exit = false;
            while (!exit) {
                System.out.println();
                System.out.println("1. View all students");
                System.out.println("2. Add new student");
                System.out.println("3. Change tokens");
                System.out.println("4. Remove student");
                System.out.println("5. Test REST API simulation");
                System.out.println("0. Exit");
                System.out.print("Choose: ");

                String choice = scanner.nextLine().trim();

                try {
                    switch (choice) {
                        case "1":
                            List<Student> all = studentService.getAllStudents();
                            System.out.println("All students:");
                            all.forEach(s ->
                                    System.out.printf("  %s %s: %d tokens%n",
                                            s.getLastName(), s.getFirstName(), s.getTokens()));
                            break;

                        case "2":
                            System.out.print("Enter last name: ");
                            String lastName = scanner.nextLine().trim();
                            System.out.print("Enter first name: ");
                            String firstName = scanner.nextLine().trim();
                            studentService.addStudent(lastName, firstName);
                            System.out.println("Student added successfully!");
                            break;

                        case "3":
                            System.out.print("Student last name: ");
                            String ln = scanner.nextLine().trim();
                            System.out.print("Student first name: ");
                            String fn = scanner.nextLine().trim();
                            System.out.print("Token change (positive or negative): ");
                            int delta = Integer.parseInt(scanner.nextLine().trim());
                            studentService.changeTokens(ln, fn, delta);
                            System.out.println("Tokens updated successfully!");
                            break;

                        case "4":
                            System.out.print("Student last name to remove: ");
                            String remLn = scanner.nextLine().trim();
                            System.out.print("Student first name to remove: ");
                            String remFn = scanner.nextLine().trim();
                            studentService.removeStudent(remLn, remFn);
                            System.out.println("Student removed successfully!");
                            break;

                        case "5":
                            System.out.println("REST API Endpoints that would be available:");
                            System.out.println("  GET /api/v1/getStatus");
                            System.out.println("  GET /api/v1/students?role=teacher");
                            System.out.println("  GET /api/v1/students/{lastName}/{firstName}?role=student");
                            System.out.println("  POST /api/v1/students?role=teacher");
                            System.out.println("  PUT /api/v1/students/{lastName}/{firstName}/tokens?role=teacher");
                            System.out.println("  DELETE /api/v1/students/{lastName}/{firstName}?role=teacher");
                            break;

                        case "0":
                            exit = true;
                            break;

                        default:
                            System.out.println("Invalid option, try again.");
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    e.printStackTrace(); // Для отладки
                }
            }
            scanner.close();
        } catch (Exception e) {
            System.err.println("Failed to start application: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Завершение работы
            UserContext.clear();
            if (context != null) {
                context.close();
            }
            System.out.println("Application finished!");
        }
    }
} 