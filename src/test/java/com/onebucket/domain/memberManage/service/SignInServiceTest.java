package com.onebucket.domain.memberManage.service;

import com.onebucket.global.auth.jwtAuth.component.JwtProvider;
import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * <br>package name   : com.onebucket.domain.memberManage.service
 * <br>file name      : SignInServiceImplTest
 * <br>date           : 2024-06-27
 * <pre>
 * <span style="color: white;">[description]</span>
 * Test of {@link SignInServiceImpl}.
 * 1 success and 1 fail, throw AuthenticationException.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-06-27        jack8              init create
 * </pre>
 */

@ExtendWith(MockitoExtension.class)
class SignInServiceTest {
    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private SignInServiceImpl signInService;


    @Test
    @DisplayName("로그인 성공")
    void testSignInByUsernameAndPassword_success() throws AuthenticationException {
        //given
        String username = "testuser";
        String password = "password";
        Authentication authentication = mock(Authentication.class);
        JwtToken jwtToken = new JwtToken("Bearer", "access-token", "refresh-token");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtProvider.generateToken(authentication)).thenReturn(jwtToken);

        //when
        JwtToken result = signInService.signInByUsernameAndPassword(username, password);

        //then
        Assertions.assertNotNull(result);
        assertThat(result.getGrantType()).isEqualTo("Bearer");
        assertThat(result.getAccessToken()).isEqualTo("access-token");
        assertThat(result.getRefreshToken()).isEqualTo("refresh-token");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtProvider).generateToken(authentication);
    }

    @Test
    @DisplayName("로그인 실패 - 유효하지 않은 값")
    void testSignInByUsernameAndPassword_fail_invalidValue() throws AuthenticationException {
        //given
        String username = "invaliduser";
        String password = "password";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("invalid credentials") {});

        //when & then
        assertThrows(AuthenticationException.class, () ->
                signInService.signInByUsernameAndPassword(username, password));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtProvider, never()).generateToken(any(Authentication.class));
    }

}