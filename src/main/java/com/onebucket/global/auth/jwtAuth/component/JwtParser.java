package com.onebucket.global.auth.jwtAuth.component;

import com.onebucket.global.auth.springSecurity.CustomAuthentication;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;

/**
 * <br>package name   : com.onebucket.gloabal.auth.jwtAuth.component
 * <br>file name      : JwtValidator
 * <br>date           : 2024-06-20
 * <pre>
 * <span style="color: white;">[description]</span>
 * Validator of JWT, also use when get authentication from jwt implementation by
 * getAuthentication method.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 * private final JwtValidator jwtValidator;
 * try {
 *     jwtValidator.isTokenValid(token);
 * } catch(JwtException e) {
 *     system.out.println(e.getMessage());
 * }
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-06-24        jack8              init create
 * 2024-06-26        jack8              add getAuthentication
 * </pre>
 */

@Component
public class JwtParser {

    private final Key key;
    private static final String AUTHORITIES_KEY = "auth";

    /**
     * Get secret Key by parameter, and change to Key type with hmacshakeyfor(keybyte);
     * @param secretKey - which in application.properties
     */
    public JwtParser(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * @param token to validate
     * @return always true, when token is invalid, throw exception.
     * @throws io.jsonwebtoken.JwtException jwtException
     */
    public boolean isTokenValid(String token) {
        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return true;
    }

    public String getRefreshToken(String jwtToken) {
        System.out.println(jwtToken);
        if (jwtToken == null || !jwtToken.startsWith("Bearer ")) {
            throw new AuthenticationException(AuthenticationErrorCode.NON_VALID_TOKEN,
                    "can't access token");
        }
        String refreshToken = jwtToken.substring(7);
        try {
            isTokenValid(refreshToken);
        } catch (ExpiredJwtException ignore) {
        } catch (Exception e) {
            throw new AuthenticationException(AuthenticationErrorCode.NON_VALID_TOKEN,
                    "token form invalid.");
        }
        return refreshToken;
    }

    /**
     * Get authentication from jwt. Use private method called {@link JwtParser parseCalims} method.
     * @param accessToken extract from this token
     * @return Authentication
     * @throws NullPointerException when claims are empty.
     * @throws io.jsonwebtoken.JwtException when token is invalid.
     */
    public CustomAuthentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        if(claims.get(AUTHORITIES_KEY) == null) {
            throw new AuthenticationException(AuthenticationErrorCode.NOT_EXIST_AUTHENTICATION_IN_TOKEN, "can't validate");
        }

        // '.'으로 구분된 권한을 List 로 생성. hasRole 권한 처리를 위함.
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(
                        claims.get(AUTHORITIES_KEY).toString().split("\\."))
                        .map(SimpleGrantedAuthority::new)
                        .toList();
        Long userId = claims.get("userId", Long.class);
        Long univId = claims.get("univId", Long.class);

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new CustomAuthentication(principal, "", authorities, userId, univId);
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.get("userId",Long.class);
    }

    private Claims parseClaims(String accessToken) {
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch(ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
