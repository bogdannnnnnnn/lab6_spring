# Проект lab6_spring - Инструкция по запуску

## Описание
Spring MVC проект с поддержкой консольного и веб-режимов работы для управления студентами.

## Предварительные требования

1. **Java 23** (проект обновлен для совместимости)
2. **Apache Maven** 3.6+
3. **IntelliJ IDEA** (рекомендуется)
4. **Apache Tomcat 10** (только для веб-режима)

## Исправленные проблемы

### ❌ Проблема: `NoClassDefFoundError: jakarta/servlet/ServletContext`
**Причина:** Консольная конфигурация загружала веб-компоненты Spring, требующие servlet API.

**✅ Решение 1:** Обновлена `ConsoleConfig.java` с исключением веб-компонентов из сканирования.
**✅ Решение 2:** Создана `SimpleConsoleConfig.java` - полностью изолированная конфигурация без ComponentScan.
**✅ Решение 3:** Изменен scope jakarta.servlet-api с `provided` на `compile`.

### ❌ Проблема: Несовместимость версий Java
**Причина:** pom.xml был настроен на Java 17, а система использует Java 23.

**✅ Решение:** Обновлены настройки Maven для Java 23.

## Способы запуска

### 1. Консольное приложение (рекомендуется для тестирования)

**Через IntelliJ IDEA:**
1. Откройте проект в IntelliJ IDEA
2. Найдите класс `TestApplication.java` в `src/main/java/ru/bmstu/testapp/`
3. Нажмите правой кнопкой → Run 'TestApplication.main()' или Ctrl+Shift+F10

**Через Maven (если настроен PATH):**
```bash
cd C:\Users\bogda\IdeaProjects\lab6_spring
mvn compile exec:java
```

**Через Maven Wrapper (если есть):**
```bash
./mvnw compile exec:java
```

### 2. Веб-приложение

**Сборка WAR файла:**
```bash
mvn clean package
```

**Деплой в Tomcat:**
1. Скопируйте `target/lab6_spring-1.0-SNAPSHOT.war` в `tomcat/webapps/`
2. Запустите Tomcat
3. Доступ по адресу: `http://localhost:8080/lab6_spring-1.0-SNAPSHOT/`

## Функциональность

### Консольное меню:
1. **View all students** - просмотр всех студентов
2. **Add new student** - добавление студента
3. **Change tokens** - изменение токенов
4. **Remove student** - удаление студента
5. **Test REST API simulation** - просмотр REST endpoints
0. **Exit** - выход

### REST API endpoints:
- `GET /api/v1/getStatus` - статус приложения
- `GET /api/v1/students?role=teacher` - список студентов
- `GET /api/v1/students/{lastName}/{firstName}?role=student` - данные студента
- `POST /api/v1/students?role=teacher` - добавление студента
- `PUT /api/v1/students/{lastName}/{firstName}/tokens?role=teacher` - изменение токенов
- `DELETE /api/v1/students/{lastName}/{firstName}?role=teacher` - удаление студента

## Архитектура проекта

```
src/main/java/ru/bmstu/testapp/
├── TestApplication.java       # Главный класс консольного приложения
├── config/
│   ├── ConsoleConfig.java    # Конфигурация для консольного режима
│   ├── WebConfig.java        # Конфигурация для веб-режима  
│   ├── AppConfig.java        # Общая конфигурация
│   └── WebAppInitializer.java# Инициализатор веб-приложения
├── controller/               # REST контроллеры
├── service/                  # Бизнес-логика
├── dao/                      # Доступ к данным
├── model/                    # Модели данных
├── dto/                      # Data Transfer Objects
└── aspect/                   # AOP аспекты
```

## Устранение проблем

### Maven не найден
```bash
# Проверьте установку Maven
mvn --version

# Если не установлен, используйте IntelliJ IDEA
# или установите Maven и добавьте в PATH
```

### Ошибки компиляции
```bash
# Очистка и пересборка
mvn clean compile

# Принудительное обновление зависимостей
mvn clean install -U
```

### Проблемы с кодировкой
В IntelliJ IDEA:
1. File → Settings → Editor → File Encodings
2. Установите UTF-8 для всех полей

## Примечания

- Проект настроен на Java 23 для совместимости с вашей системой
- Консольный режим полностью изолирован от веб-зависимостей
- Используется Spring Framework 6.1.5
- Поддержка AOP с AspectJ для логирования и безопасности

## Запуск тестов

```bash
mvn test
```

Проект готов к использованию! Рекомендуется начать с консольного режима. # lab6_spring
