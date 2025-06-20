package ru.bmstu.testapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

/**
 * Консольная конфигурация Spring - полностью изолированная от веб-компонентов
 */
@Configuration
@ComponentScan(basePackages = {
    "ru.bmstu.testapp.service",
    "ru.bmstu.testapp.dao", 
    "ru.bmstu.testapp.model",
    "ru.bmstu.testapp.aspect"
}, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*Web.*"),
    @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*Controller.*"),
    @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*Mvc.*")
})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ConsoleConfig {

    /**
     * Чтобы @Value("${...}") из application.properties работал,
     * регистрируем PropertySourcesPlaceholderConfigurer.
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("application.properties"));
        configurer.setIgnoreUnresolvablePlaceholders(true);
        configurer.setIgnoreResourceNotFound(true); // Игнорировать отсутствие файла
        return configurer;
    }
} 