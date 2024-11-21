package com.onebucket.domain.memberManage.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.onebucket.domain.memberManage.dto.SignInRequestDto;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.memberManage.service.SignInService;
import com.onebucket.global.auth.jwtAuth.component.JwtProvider;
import com.onebucket.global.auth.jwtAuth.component.JwtParser;
import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import com.onebucket.global.auth.jwtAuth.service.RefreshTokenService;
import com.onebucket.global.exceptionManage.customException.CommonException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.CommonErrorCode;
import com.onebucket.global.exceptionManage.errorCode.ValidateErrorCode;
import com.onebucket.global.exceptionManage.exceptionHandler.BaseExceptionHandler;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static com.onebucket.testComponent.testUtils.JsonFieldResultMatcher.hasKey;
import static com.onebucket.testComponent.testUtils.JsonFieldResultMatcher.hasStatus;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    JwtParser jwtParser;
    @Mock
    JwtProvider jwtProvider;
    @Mock
    private MemberService memberService;
    @InjectMocks
    private SignInController signInController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(signInController)
                .setControllerAdvice(new BaseExceptionHandler(), new DataExceptionHandler())
                .build();
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("signIn - success")
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
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(hasKey(token));

        verify(refreshTokenService, times(1)).saveRefreshToken(anyString());
    }

    @Test
    @DisplayName("signIn - fail / validation error")
    void testSignIn_fail_invalidValue() throws Exception {
        //when
        String username = "";
        String password = "";

        SignInRequestDto dto = new SignInRequestDto(username, password);
        ValidateErrorCode code = ValidateErrorCode.INVALID_DATA;

        mockMvc.perform(post("/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code))
                .andExpect(content().string(containsString("username: username must not be empty")))
                .andExpect(content().string(containsString("password: password must not be empty")));

        verify(refreshTokenService, never()).saveRefreshToken(anyString());
    }


    //해당 테스트 코드는 signInService 의 signInByUsernameAndPassword 에서 발생하는 예외를 처리하는 테스트 코드이다.
    //해당 메서드에서 발생시키는 예외는 AuthenticationException(spring security 의) 전반에 걸쳐 있지만,
    //로직은 유사하므로 해당 예외만 테스트하도록 한다.
    @Test
    @DisplayName("signIn - fail / wrong username or password")
    void testSignIn_fail_notExistUser() throws  Exception {
        //when
        String username = "test user";
        String password = "invalid password";
        SignInRequestDto dto = new SignInRequestDto(username, password);

        AuthenticationErrorCode code = AuthenticationErrorCode.CREDENTIAL_INVALID;

        when(signInService.signInByUsernameAndPassword(username, password)).thenThrow(
                new AuthenticationException(code)
        );

        mockMvc.perform(post("/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));

        verify(refreshTokenService, never()).saveRefreshToken(anyString());
    }

    @Test
    @DisplayName("signIn - fail / token invalid while save in redis ")
    void testSignIn_fail_cannotSaveRefreshToken() throws Exception {
        //when
        String username = "test user";
        String password = "password";
        SignInRequestDto dto = new SignInRequestDto(username, password);

        JwtToken token = new JwtToken("Bearer", "new access", "new refresh");

        AuthenticationErrorCode code = AuthenticationErrorCode.NON_VALID_TOKEN;
        String internalMessage = "username or refresh token is null";
        AuthenticationException exception = new AuthenticationException(code, internalMessage);

        when(signInService.signInByUsernameAndPassword(username, password)).thenReturn(token);
        doThrow(exception).when(refreshTokenService).saveRefreshToken(anyString());

        mockMvc.perform(post("/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code, internalMessage));

    }

    @Test
    @DisplayName("signIn - fail / connection of redis close")
    void testSignIn_fail_redisConnectionFail() throws Exception {
        String username = "test user";
        String password = "password";
        SignInRequestDto dto = new SignInRequestDto(username, password);

        JwtToken token = new JwtToken("Bearer", "new access", "new refresh");

        CommonErrorCode code = CommonErrorCode.REDIS_CONNECTION_ERROR;
        String internalMessage = "connection fail while save token in redis";
        CommonException exception = new CommonException(code, internalMessage);

        when(signInService.signInByUsernameAndPassword(username, password)).thenReturn(token);
        doThrow(exception).when(refreshTokenService).saveRefreshToken(anyString());

        mockMvc.perform(post("/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code, internalMessage));

    }

    //-+-+-+-+-+-+]] tokenRefresh test [[-+-+-+-+-+-+
    @Test
    @DisplayName("tokenRefresh - success")
    void testTokenRefresh_success() throws Exception {

        String refreshToken = "refresh token";
        JwtToken newToken =
                new JwtToken("grantType", "new access token", "new refresh token");
        when(jwtProvider.generateToken(anyString())).thenReturn(newToken);

        mockMvc.perform(post("/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(newToken)))
                .andDo(print())
                .andExpect(hasKey(newToken));

        verify(refreshTokenService, times(1)).saveRefreshToken(
                anyString()
        );

    }

    @Test
    @DisplayName("tokenRefresh - fail / refresh token null")
    void testTokenRefresh_fail_cannotFindAccessToken() throws Exception {
        AuthenticationErrorCode code = AuthenticationErrorCode.NON_VALID_TOKEN;
        AuthenticationException authenticationException = new AuthenticationException(code,"can't access token");
        doThrow(authenticationException).when(jwtProvider).generateToken("Bearer " + null);
        JwtToken newToken =
                new JwtToken("grantType", "new access token", "new refresh token");


        mockMvc.perform(post("/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + null)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(newToken)))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));

        verify(jwtParser, never()).isTokenValid(anyString());
    }

    @Test
    @DisplayName("tokenRefresh - fail / token invalid")
    void testTokenRefresh_fail_refreshTokenInvalid() throws Exception {

        String refreshToken = "invalid refresh token";
        JwtToken newToken =
                new JwtToken("grantType", "new access token", refreshToken);

        AuthenticationErrorCode code = AuthenticationErrorCode.NON_VALID_TOKEN;
        AuthenticationException authenticationException = new AuthenticationException(code,"can't access token");


        when(jwtProvider.generateToken(refreshToken)).thenThrow(authenticationException);
        mockMvc.perform(post("/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(newToken)))
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));

        verify(refreshTokenService, never()).saveRefreshToken(
                anyString()
        );

    }


}