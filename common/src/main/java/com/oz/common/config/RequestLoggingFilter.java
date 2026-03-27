package com.oz.common.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;


@Component
public class RequestLoggingFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        logger.info("Запрос: {} {}", httpRequest.getMethod(), httpRequest.getRequestURI());

        long start = System.currentTimeMillis();
        try {
            chain.doFilter(request, response); // дальше по цепочке (к контроллеру)
        } finally {
            long duration = System.currentTimeMillis() - start;
            logger.info("Ответ отправлен, время обработки: {} мс", duration);
            MDC.clear();
        }
    }

}
