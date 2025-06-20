package ru.bmstu.testapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import ru.bmstu.testapp.aspect.RoleCheckAspect;
import ru.bmstu.testapp.dao.StudentDao;
import ru.bmstu.testapp.dao.StudentDaoCsvImpl;
import ru.bmstu.testapp.service.AuditService;
import ru.bmstu.testapp.service.StudentService;
import ru.bmstu.testapp.service.impl.AuditServiceImpl;
import ru.bmstu.testapp.service.impl.StudentServiceImpl;

/**
 * Простая консольная конфигурация без ComponentScan
 * Регистрирует бины вручную чтобы избежать автоконфигурации веб-компонентов
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SimpleConsoleConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("application.properties"));
        configurer.setIgnoreUnresolvablePlaceholders(true);
        configurer.setIgnoreResourceNotFound(true);
        return configurer;
    }

    @Bean
    public StudentDao studentDao() {
        return new StudentDaoCsvImpl();
    }

    @Bean
    public AuditService auditService() {
        return new AuditServiceImpl();
    }

    @Bean
    public StudentService studentService(StudentDao studentDao, AuditService auditService) {
        return new StudentServiceImpl(studentDao, auditService);
    }

    @Bean
    public RoleCheckAspect roleCheckAspect() {
        return new RoleCheckAspect();
    }
} 