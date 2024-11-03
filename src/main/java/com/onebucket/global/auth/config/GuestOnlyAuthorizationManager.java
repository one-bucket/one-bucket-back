package com.onebucket.global.auth.config;

import java.util.function.Supplier;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

@Component
public class GuestOnlyAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private static final AuthorizationDecision DENY = new AuthorizationDecision(false);
    private static final AuthorizationDecision ALLOW = new AuthorizationDecision(true);

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        String url = context.getRequest().getRequestURI();
        // 권한이 GUEST 만 가지고 있고, 접근 url 에 "/guest/" 가 포함되지 않은 경우
        boolean isGuestOnly = authentication.get().getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_GUEST")) &&
                authentication.get().getAuthorities().size() == 1;
        if(isGuestOnly && !url.startsWith("/guest/")) {
            return DENY;
        }
        return ALLOW;
    }
}
