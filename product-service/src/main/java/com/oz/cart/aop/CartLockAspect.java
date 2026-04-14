package com.oz.cart.aop;

import com.oz.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Aspect
@Component
@RequiredArgsConstructor
public class CartLockAspect {

    private final RedissonClient redissonClient;

    @Around("@annotation(lockableCart)")
    public Object locking(ProceedingJoinPoint joinPoint,LockableCart lockableCart) throws Throwable {
        String cartKey = findIdFromAnnotation(joinPoint, lockableCart);
        String fullLockKey = "cart_lock:" + cartKey;
        RLock lock = redissonClient.getLock(fullLockKey);
        boolean acquired = lock.tryLock(5, 10, TimeUnit.SECONDS);
        if (!acquired) {
            throw new CustomException("Система занята, попробуйте позже");
        }

        try {
            return joinPoint.proceed(); // Выполняем сам метод сервиса
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private String findIdFromAnnotation(ProceedingJoinPoint joinPoint,LockableCart lockableCart){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] parameterNames  = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        String targetName = lockableCart.cartId();
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(targetName)) {
                return args[i].toString();
            }
        }

        throw new CustomException("Параметр с именем " + targetName + " не найден");
    }
}
