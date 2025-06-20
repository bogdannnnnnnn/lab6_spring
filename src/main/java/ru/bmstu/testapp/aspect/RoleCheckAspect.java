package ru.bmstu.testapp.aspect;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import ru.bmstu.testapp.model.Role;

@Aspect
@Component
public class RoleCheckAspect {

    @Around("@annotation(ru.bmstu.testapp.aspect.RequireRole)")
    public Object checkRole(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        Method method = sig.getMethod();
        RequireRole rr = method.getAnnotation(RequireRole.class);

        Role modelRole = UserContext.getRole();
        if (modelRole == null) {
            throw new SecurityException("No role set. Access denied.");
        }
        
        // Сопоставляем модельную роль с RoleType
        RoleType current = (modelRole == Role.TEACHER ? RoleType.TEACHER : RoleType.STUDENT);

        boolean allowed = Arrays.stream(rr.value()).anyMatch(r -> r == current);
        if (!allowed) {
            throw new SecurityException("Access denied for role: " + modelRole);
        }
        return pjp.proceed();
    }
} 