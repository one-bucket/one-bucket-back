package com.onebucket.global.auth.jwtAuth.component;

import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import com.onebucket.testComponent.mockmember.MockMember;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <br>package name   : com.onebucket.gloabal.auth.jwtAuth.component
 * <br>file name      : JwtProviderTest
 * <br>date           : 2024-06-20
 * <pre>
 * <span style="color: white;">[description]</span>
 * Test of JwtProvider / using  mockito in gradle
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
 * 2024-06-20        jack8              init create
 * </pre>
 */

@SpringJUnitConfig
class JwtProviderTest {

    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        String secretKey = "bXlzZWNyZXRrZXlteXNlY3JldGtleW15c2VjcmV0a2V5bXlzZWNyZXRrZXk=";
        long expireDateAccessToken = 3600000L;
        long expireDateRefreshToken = 86400000L;

        jwtProvider = new JwtProvider(secretKey, expireDateAccessToken, expireDateRefreshToken);

    }

    @Test
    @DisplayName("정상적인 토큰 생성")
    @MockMember
    void testGenerateToken() {
        //given
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //when
        JwtToken jwtToken = jwtProvider.generateToken(authentication);

        //then
        assertNotNull(jwtToken);
        assertEquals("Bearer", jwtToken.getGrantType());
        assertNotNull(jwtToken.getAccessToken());
        assertNotNull(jwtToken.getRefreshToken());

        //decoding token to validate.
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode("bXlzZWNyZXRrZXlteXNlY3JldGtleW15c2VjcmV0a2V5bXlzZWNyZXRrZXk="));

        Claims claims = Jwts.parserBuilder().
                setSigningKey(key).build().parseClaimsJws(jwtToken.getAccessToken()).getBody();
        assertEquals("test user", claims.getSubject());


    }

    @Test
    @DisplayName("승인받지 않은 사용자의 토큰 생성")
    @WithAnonymousUser
    void testGenerateToken_invalid() {
        //given
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThrows(IllegalArgumentException.class, () ->
                jwtProvider.generateToken(authentication));
    }
}