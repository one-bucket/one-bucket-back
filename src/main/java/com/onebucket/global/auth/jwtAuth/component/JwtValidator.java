package com.onebucket.global.auth.jwtAuth.component;

import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.RegisterException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
public class JwtValidator {

    private final Key key;
    private static final String AUTHORITIES_KEY = "auth";

    /**
     * Get secret Key by parameter, and change to Key type with hmacshakeyfor(keybyte);
     * @param secretKey - which in application.properties
     */
    public JwtValidator(@Value("${jwt.secret}") String secretKey) {
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

    /**
     * Get authentication from jwt. Use private method called {@link JwtValidator parseCalims} method.
     * @param accessToken extract from this token
     * @return Authentication
     * @throws NullPointerException when claims are empty.
     * @throws io.jsonwebtoken.JwtException when token is invalid.
     */
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        if(claims.get(AUTHORITIES_KEY) == null) {
            throw new RegisterException(AuthenticationErrorCode.NOT_EXIST_AUTHENTICATION_IN_TOKEN, "can't validate");
        }

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(
                claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
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
