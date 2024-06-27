package com.onebucket.global.auth.config;

import com.onebucket.global.auth.jwtAuth.component.JwtValidator;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultHeader;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <br>package name   : com.onebucket.global.auth.config
 * <br>file name      : JwtAuthenticationFilterTest
 * <br>date           : 2024-06-26
 * <pre>
 * <span style="color: white;">[description]</span>
 *
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
 * 2024-06-26        jack8              init create
 * </pre>
 */
@WebMvcTest
@ExtendWith(MockitoExtension.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class JwtAuthenticationFilterTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtValidator jwtValidator;


    @Test
    @DisplayName("필터 내 토큰 검증")
    void testValidToken() throws Exception {
        //given
        UserDetails userDetails = new User("user", "", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

        when(jwtValidator.isTokenValid(anyString())).thenReturn(true);
        when(jwtValidator.getAuthentication(anyString())).thenReturn(authentication);      //when & then
        mockMvc.perform(get("/security-endpoint")
                .header("Authorization", "Bearer validToken"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("필터 내 비정상적인 토큰 검증")
    void testInvalidToken() throws Exception {
        //given
        when(jwtValidator.isTokenValid(anyString())).thenThrow(new SignatureException("invalid token"));
        mockMvc.perform(get("/security-endpoint")
                .header("Authorization", "Bearer invalidToken"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("토큰이 없을 경우")
    void testNoToken() throws Exception {
        mockMvc.perform(get("/security-endpoint"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("만료된 토큰의 경우")
    void testExpiredToken() throws Exception {
        //given
        Header header = new DefaultHeader();
        DefaultClaims claims = new DefaultClaims();
        when(jwtValidator.isTokenValid(anyString())).thenThrow(new ExpiredJwtException(header, claims, "Token Expired"));
        //when & then
        mockMvc.perform(get("/security-endpoint")
                .header("Authorization", "Bearer expiredToken"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("permitAll url 테스트")
    void testExcludeUrl() throws Exception {
        //when & then
        mockMvc.perform(get("/test/url"))
                .andExpect(status().isOk());
    }
}