package com.oz.common.config;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

public class MdcTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        // Берем контекст из текущего (родительского) потока
        Map<String, String> contextMap = MDC.getCopyOfContextMap();

        return () -> {
            try {
                // Устанавливаем его в новом (дочернем) потоке
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }
                runnable.run();
            } finally {
                // Обязательно очищаем после выполнения
                MDC.clear();
            }
        };
    }
}