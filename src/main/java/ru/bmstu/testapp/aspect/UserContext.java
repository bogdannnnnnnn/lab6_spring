package ru.bmstu.testapp.aspect;

import ru.bmstu.testapp.model.Role;

/**
 * Хранит текущую роль пользователя в ThreadLocal.
 * В веб-приложении роль устанавливается из параметров запроса,
 * а в конце обработки запроса – очищается.
 */
public class UserContext {
    private static final ThreadLocal<Role> currentRole = new ThreadLocal<>();

    public static void setRole(Role role) {
        currentRole.set(role);
    }

    public static Role getRole() {
        return currentRole.get();
    }

    public static void clear() {
        currentRole.remove();
    }
} 