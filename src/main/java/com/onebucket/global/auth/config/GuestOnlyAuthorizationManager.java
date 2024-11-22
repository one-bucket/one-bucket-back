package com.onebucket.global.auth.config;

import java.util.function.Supplier;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

/**
 * GUEST 권한만 가지고 있는 유저가 접근하려는 url 에 "/guest/" 가 포함되지 않은 경우 403 에러를 반환한다.
 */
@Component
public class GuestOnlyAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private static final AuthorizationDecision DENY = new AuthorizationDecision(false);
    private static final AuthorizationDecision ALLOW = new AuthorizationDecision(true);

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        String url = context.getRequest().getRequestURI();
        boolean hasGuestOnly = authentication.get().getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_GUEST")) &&
                authentication.get().getAuthorities().size() == 1;

        if(hasGuestOnly && !url.startsWith("/guest/")) {
            return DENY;
        }

        return ALLOW;
    }
}
