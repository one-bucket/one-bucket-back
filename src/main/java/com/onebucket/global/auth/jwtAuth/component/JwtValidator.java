package com.onebucket.global.auth.jwtAuth.component;

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
 * packageName : <span style="color: orange;">com.onebucket.gloabal.auth.jwtAuth.component</span> <br>
 * name : <span style="color: orange;">JwtValidator</span> <br>
 * <p>
 * <span style="color: white;">[description]</span>
 * </p>
 * see Also: <br>
 *
 * <pre>
 * code usage:
 * {@code
 *
 * }
 * modified log:
 * ==========================================================
 * DATE          Author           Note
 * ----------------------------------------------------------
 * 6/21/24        isanghyeog         first create
 * 6/25/24        sang               add getAuthentication
 *
 * </pre>
 */

@Component
public class JwtValidator {

    private final Key key;
    private static final String AUTHORITIES_KEY = "auth";

    public JwtValidator(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token) {
        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return true;
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        if(claims.get(AUTHORITIES_KEY) == null) {
            throw new NullPointerException();
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
