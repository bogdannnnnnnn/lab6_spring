package ru.bmstu.testapp.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {
    /**
     * Список ролей, которым разрешён вызов метода.
     * (Используется в RoleCheckAspect.)
     */
    RoleType[] value();
} 