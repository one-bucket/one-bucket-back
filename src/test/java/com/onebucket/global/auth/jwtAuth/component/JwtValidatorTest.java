package com.onebucket.global.auth.jwtAuth.component;

import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;


import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


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
@SpringJUnitConfig
class JwtValidatorTest {

    private JwtValidator jwtValidator;
    private final String secretKey = "bXlzZWNyZXRrZXlteXNlY3JldGtleW15c2VjcmV0a2V5bXlzZWNyZXRrZXk=";


    private String createToken(long expireDate, List<String> authorities, String inKey) {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(inKey));
        String authoritiesString = String.join(".", authorities);

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
        assertThrows(ExpiredJwtException.class, ()-> jwtValidator.isTokenValid(token));
    }

    @Test
    @DisplayName("비정상적인 토큰 - SignatureException")
    void testIsWrongSig() {
        final String wrongKey = "wrongKeywrongKeywrongKeywrongKeywrongKeywrongKeywrongKeywrongKeywrongKeywrongKey";
        //when
        String token = createToken(10000, Arrays.asList("ROLE_USER", "ROLE_ADMIN"), wrongKey);

        //then
        assertThrows(SignatureException.class, ()-> jwtValidator.isTokenValid(token));
    }

    @Test
    @DisplayName("비정상적인 토큰 - MalformedJwtException")
    void testIsMalformed() {
        //when
        String token = createToken(10000, Arrays.asList("ROLE_USER", "ROLE_ADMIN"), secretKey).replace(".", "|");
        //then
        assertThrows(MalformedJwtException.class, ()-> jwtValidator.isTokenValid(token));
    }

    @Test
    @DisplayName("정상적인 권한 정보 접근")
    void testGetAuthentication() {
        String token = createToken(10000, Arrays.asList("ROLE_USER", "ROLE_ADMIN"), secretKey);
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) jwtValidator.getAuthentication(token);

        assertNotNull(authentication);
        assertEquals("testuser",((UserDetails) authentication.getPrincipal()).getUsername());
        assertEquals(2, authentication.getAuthorities().size());
    }

    @Test
    @DisplayName("비정상적인 권한 정보 접근")
    void testGetAuthenticationWithNoAuthorities() {
        String token =  Jwts.builder()
                .setSubject("testuser")
                .setExpiration(new Date(System.currentTimeMillis() + 10000))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)), SignatureAlgorithm.HS256)
                .compact();

        //when & then
        assertThrows(AuthenticationException.class, () -> jwtValidator.getAuthentication(token));
    }




}