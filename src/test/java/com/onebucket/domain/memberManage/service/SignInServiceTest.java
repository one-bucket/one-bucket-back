package com.onebucket.domain.memberManage.service;

import com.onebucket.global.auth.jwtAuth.component.JwtProvider;
import com.onebucket.global.auth.jwtAuth.component.JwtParser;
import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private JwtParser jwtParser;

    @InjectMocks
    private SignInServiceImpl signInService;


    @Test
    @DisplayName("signIn - success")
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
    @DisplayName("signIn - fail / invalid user")
    void testSignInByUsernameAndPassword_fail_invalidValue() {
        //given
        String username = "invaliduser";
        String password = "password";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException(""));


        //when & then
        assertThatThrownBy(() -> signInService.signInByUsernameAndPassword(username, password))
                .isInstanceOf(AuthenticationException.class)
                .extracting("errorCode")
                .isEqualTo(AuthenticationErrorCode.CREDENTIAL_INVALID);


        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtProvider, never()).generateToken(any(Authentication.class));
    }

//    @Test
//    @DisplayName("getAuthenticationAndValidHeader - success")
//    void testGetAuthenticationAndValidHeader_success() {
//        String headerString = "Bearer access-token";
//        CustomAuthentication authentication = mock(CustomAuthentication.class);
//        when(jwtValidator.getAuthentication("access-token")).thenReturn(authentication);
//
//
//        Authentication newAuth = signInService.getAuthenticationAndValidHeader(headerString);
//        assertThat(newAuth).isEqualTo(authentication);
//
//    }
//
//    @Test
//    @DisplayName("getAuthenticationAndValidHeader - fail / token invalid")
//    void testAuthenticationAndValidHeader_fail_invalidToken() {
//        String headerString = "invalid-token";
//         assertThatThrownBy(() -> signInService.getAuthenticationAndValidHeader(headerString))
//                 .isInstanceOf(AuthenticationException.class)
//                 .extracting("errorCode")
//                 .isEqualTo(AuthenticationErrorCode.NON_VALID_TOKEN);
//    }
//
//    @Test
//    @DisplayName("getAuthenticationAndValidHeader - fail /  exception while valid token")
//    void testAuthenticationAndValidHeader_fail_validFail() {
//        String headerString = "Bearer invalid-token";
//
//        when(jwtValidator.isTokenValid("invalid-token")).thenThrow(new RuntimeException());
//
//        assertThatThrownBy(() -> signInService.getAuthenticationAndValidHeader(headerString))
//                .isInstanceOf(AuthenticationException.class)
//                .extracting("errorCode")
//                .isEqualTo(AuthenticationErrorCode.NON_VALID_TOKEN);
//    }

}