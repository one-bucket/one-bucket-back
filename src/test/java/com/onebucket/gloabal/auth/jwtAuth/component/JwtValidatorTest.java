package com.onebucket.gloabal.auth.jwtAuth.component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <br>package name   : com.onebucket.gloabal.auth.jwtAuth.component
 * <br>file name      : JwtValidatorTest
 * <br>date           : 2024-06-24
 * <pre>
 * <span style="color: white;">[description]</span>
 * Test {@link JwtValidator}.
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
 * 2024-06-24        jack8              init create
 * </pre>
 */
class JwtValidatorTest {

    private JwtValidator jwtValidator;
    private final String secretKey = "bXlzZWNyZXRrZXlteXNlY3JldGtleW15c2VjcmV0a2V5bXlzZWNyZXRrZXk=";


    private String createToken(long expireDate, List<String> authorities, String inKey) {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(inKey));
        String authoritiesString = String.join(",", authorities);

        return Jwts.builder()
                .setSubject("testuser")
                .setExpiration(new Date(System.currentTimeMillis() + expireDate))
                .claim("auth", authoritiesString)
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }

    @BeforeEach
    void setUp() {
        jwtValidator = new JwtValidator(secretKey);
    }

    @Test
    @DisplayName("정상적인 토큰 검증")
    void testIsTokenValid() {
        //when
        String token = createToken(10000, Arrays.asList("ROLE_USER", "ROLE_ADMIN"), secretKey);

        //then
        assertDoesNotThrow(() -> jwtValidator.isTokenValid(token));
    }

    @Test
    @DisplayName("비정상적인 토큰 - ExpiredJwtException")
    void testIsExpired() {
        //when
        String token = createToken(0, Arrays.asList("ROLE_USER", "ROLE_ADMIN"),secretKey);

        //then
        assertThrows(ExpiredJwtException.class, ()-> {
            jwtValidator.isTokenValid(token);
        });
    }

    @Test
    @DisplayName("비정상적인 토큰 - SignatureException")
    void testIsWrongSig() {
        final String wrongKey = "wrongKeywrongKeywrongKeywrongKeywrongKeywrongKeywrongKeywrongKeywrongKeywrongKey";
        //when
        String token = createToken(10000, Arrays.asList("ROLE_USER", "ROLE_ADMIN"), wrongKey);

        //then
        assertThrows(SignatureException.class, ()-> {
            jwtValidator.isTokenValid(token);
        });
    }

    @Test
    @DisplayName("비정상적인 토큰 - MalformedJwtException")
    void testIsMalformed() {
        //when
        String token = createToken(10000, Arrays.asList("ROLE_USER", "ROLE_ADMIN"), secretKey).replace(".", "|");
        //then
        assertThrows(MalformedJwtException.class, ()-> {
            jwtValidator.isTokenValid(token);
        });
    }



}