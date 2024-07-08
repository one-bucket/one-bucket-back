package com.onebucket.domain.memberManage.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onebucket.domain.memberManage.dto.RefreshTokenDto;
import com.onebucket.domain.memberManage.dto.SignInRequestDto;
import com.onebucket.domain.memberManage.service.SignInService;
import com.onebucket.global.auth.jwtAuth.component.JwtProvider;
import com.onebucket.global.auth.jwtAuth.component.JwtValidator;
import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import com.onebucket.global.auth.jwtAuth.domain.RefreshToken;
import com.onebucket.global.auth.jwtAuth.service.RefreshTokenService;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.ValidateErrorCode;
import com.onebucket.global.exceptionManage.exceptionHandler.AuthenticationExceptionHandler;
import com.onebucket.global.exceptionManage.exceptionHandler.DataExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.onebucket.testComponent.JsonFieldResultMatcher.hasKey;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <br>package name   : com.onebucket.domain.memberManage.api
 * <br>file name      : SignInControllerTest
 * <br>date           : 2024-06-30
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
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-06-30        jack8              init create
 * </pre>
 */

@ExtendWith(MockitoExtension.class)
class SignInControllerTest {

    @Mock
    private SignInService signInService;

    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    JwtValidator jwtValidator;
    @Mock
    JwtProvider jwtProvider;

    @InjectMocks
    private SignInController signInController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(signInController)
                .setControllerAdvice(new AuthenticationExceptionHandler(), new DataExceptionHandler())
                .build();
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("로그인 테스트 성공")
    void testSignIn_success() throws Exception {
        //given
        String username = "test user";
        String password = "password";

        SignInRequestDto dto = new SignInRequestDto(username, password);
        JwtToken token = new JwtToken("Bearer", "new access", "new refresh");

        Mockito.when(signInService.signInByUsernameAndPassword(username, password)).thenReturn(token);

        //when & then
        mockMvc.perform(post("/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(hasKey(token));

        verify(refreshTokenService, times(1)).saveRefreshToken(any(RefreshToken.class));
    }

    @Test
    @DisplayName("로그인 테스트 실패 - 유효성 검사 실패")
    void testSignIn_fail_invalidValue() throws Exception {
        //when
        String username = "";
        String password = "";

        SignInRequestDto dto = new SignInRequestDto(username, password);


        mockMvc.perform(post("/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(hasKey(ValidateErrorCode.INVALID_DATA))
                .andExpect(content().string(containsString("username: username must not be empty")))
                .andExpect(content().string(containsString("password: password must not be empty")));

        verify(refreshTokenService, never()).saveRefreshToken(any(RefreshToken.class));
    }


    //해당 테스트 코드는 signInService 의 signInByUsernameAndPassword 에서 발생하는 예외를 처리하는 테스트 코드이다.
    //해당 메서드에서 발생시키는 예외는 AuthenticationException(spring security 의) 전반에 걸쳐 있지만,
    //로직은 유사하므로 해당 예외만 테스트하도록 한다.
    @Test
    @DisplayName("로그인 테스트 실패 - 아이디/비밀번호 오류")
    void testSignIn_fail_notExistUser() throws  Exception {
        //when
        String username = "test user";
        String password = "invalid password";
        SignInRequestDto dto = new SignInRequestDto(username, password);

        when(signInService.signInByUsernameAndPassword(username, password)).thenThrow(
                new AuthenticationException(AuthenticationErrorCode.CREDENTIAL_INVALID)
        );

        mockMvc.perform(post("/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                        .andExpect(hasKey(AuthenticationErrorCode.CREDENTIAL_INVALID));

        verify(refreshTokenService, never()).saveRefreshToken(any(RefreshToken.class));
    }

    @Test
    @DisplayName("로그인 테스트 실패 - refresh token 저장 로직 fail")
    void testSignIn_fail_cannotSaveRefreshToken() throws Exception {
        //when
        String username = "test user";
        String password = "password";
        SignInRequestDto dto = new SignInRequestDto(username, password);

        JwtToken token = new JwtToken("Bearer", "new access", "new refresh");

        when(signInService.signInByUsernameAndPassword(username, password)).thenReturn(token);
        doThrow(new AuthenticationException(AuthenticationErrorCode.NON_VALID_TOKEN, "error")).
                when(refreshTokenService).saveRefreshToken(any(RefreshToken.class));

        mockMvc.perform(post("/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(hasKey(AuthenticationErrorCode.NON_VALID_TOKEN, "error"));

    }

    //-+-+-+-+-+-+]] tokenRefresh test [[-+-+-+-+-+-+
    @Test
    @DisplayName("토큰 재발급 성공")
    void testTokenRefresh_success() throws Exception {

        String refreshToken = "refresh token";
        String accessToken = "access token";
        RefreshTokenDto dto = new RefreshTokenDto(refreshToken);

        String username = "test user";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "password");
        JwtToken newToken =
                new JwtToken("grantType", "new access token", "new refresh token");
        when(signInService.getAuthenticationAndValidHeader("Bearer " + accessToken)).thenReturn(authentication);
        when(jwtValidator.isTokenValid(refreshToken)).thenReturn(true);
        when(refreshTokenService.isTokenExist(any(RefreshToken.class))).thenReturn(true);
        when(jwtProvider.generateToken(authentication)).thenReturn(newToken);

        mockMvc.perform(post("/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto))
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(hasKey(newToken));

        verify(refreshTokenService, times(1)).saveRefreshToken(
                any(RefreshToken.class)
        );

    }

    @Test
    @DisplayName("토큰 재발급 실패 - access 토큰 없음")
    void testTokenRefresh_fail_cannotFindAccessToken() throws Exception {
        String refreshToken = "refresh token";
        RefreshTokenDto dto = new RefreshTokenDto(refreshToken);
        when(signInService.getAuthenticationAndValidHeader(any())).thenThrow(
                new AuthenticationException(AuthenticationErrorCode.NON_VALID_TOKEN)
        );

        mockMvc.perform(post("/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpect(hasKey(AuthenticationErrorCode.NON_VALID_TOKEN));

        verify(jwtValidator, never()).isTokenValid(anyString());
    }

    @Test
    @DisplayName("토큰 재발급 실패 - refresh token error")
    void testTokenRefresh_fail_refreshTokenInvalid() throws Exception {

        String refreshToken = "refresh token";
        String accessToken = "access token";
        RefreshTokenDto dto = new RefreshTokenDto(refreshToken);

        String username = "test user";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "password");
        when(signInService.getAuthenticationAndValidHeader("Bearer " + accessToken)).thenReturn(authentication);
        when(jwtValidator.isTokenValid(refreshToken)).thenReturn(false);

        mockMvc.perform(post("/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(hasKey(AuthenticationErrorCode.NON_VALID_TOKEN,
                        "error to validate refresh token"));

        verify(refreshTokenService, never()).saveRefreshToken(
                any(RefreshToken.class)
        );

    }


}