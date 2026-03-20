package com.oz.common.patterns.requestScope;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope // Объект живет один запрос
// ProxyMode нужен, чтобы внедрить этот короткоживущий объект в долгоживущий @Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class UserContext {
    private String userId;
    private String email;
    private boolean isVip;
}
