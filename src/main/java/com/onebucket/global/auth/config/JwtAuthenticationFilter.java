package com.onebucket.global.auth.config;

import com.onebucket.global.auth.jwtAuth.component.JwtValidator;
import com.onebucket.global.auth.jwtAuth.exception.NullJwtException;
import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

/**
 * <br>package name   : com.onebucket.global.auth.config
 * <br>file name      : JwtAuthenticationFilter
 * <br>date           : 2024-06-25
 * <pre>
 * <span style="color: white;">[description]</span>
 * FilterChain about validating jwt token and exception management about jwt.
 * Need to implement more in exceptionHandler method, which for error code and response.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-06-25        jack8              init create
 * </pre>
 */

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtValidator jwtValidator;

    private static final List<String> EXCLUDE_URLS = List.of(
            "/test/url",
            "/sign-in",
            "/register",
            "/refresh-token",
            "/member/password/reset",
            "/refresh-token",
            "/ws"
    );

    private static final List<String> EXCLUDE_URL_PREFIXES = List.of(
            "/test/",
            "/docs/",
            "/dev/"
    );

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String url = ((HttpServletRequest) servletRequest).getRequestURI();

        if(isExcluded(url)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        try {
            String token = resolveToken((HttpServletRequest) servletRequest);
            if(token != null && jwtValidator.isTokenValid(token)) {
                Authentication authentication = jwtValidator.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new NullJwtException("no token");
            }

            filterChain.doFilter(servletRequest, servletResponse);
        }  catch(JwtException e) {
            handleException(httpResponse, e);
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean isExcluded(String url) {
        if(EXCLUDE_URLS.contains(url)) {
            return true;
        }
        return EXCLUDE_URL_PREFIXES.stream().anyMatch(url::startsWith);
    }

    private void handleException(HttpServletResponse response, JwtException e) {
        //need to implement more....
        if (e instanceof ExpiredJwtException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else if (e instanceof SignatureException) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else if (e instanceof MalformedJwtException) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else if (e instanceof ClaimJwtException) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else if(e instanceof NullJwtException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
