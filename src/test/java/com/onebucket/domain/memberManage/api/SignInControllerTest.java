package com.onebucket.domain.memberManage.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onebucket.domain.memberManage.dto.SignInRequestDto;
import com.onebucket.domain.memberManage.service.SignInService;
import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import com.onebucket.global.auth.jwtAuth.domain.RefreshToken;
import com.onebucket.global.auth.jwtAuth.service.RefreshTokenService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

    @InjectMocks
    private SignInController signInController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(signInController)
                .setControllerAdvice(new AuthenticationExceptionHandler(), new DataExceptionHandler())
                .build();
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
                .andExpect(jsonPath("$.grantType").value("Bearer"))
                .andExpect(jsonPath("$.accessToken").value("new access"))
                .andExpect(jsonPath("$.refreshToken").value("new refresh"));

        verify(refreshTokenService, times(1)).saveRefreshToken(any(RefreshToken.class));
    }

    @Test
    @DisplayName("로그인 테스트 실패 - 유효성 검사 실패")
    void testSignIn_fail_invalidValue() throws Exception {
        String username = "";
        String password = "";

        SignInRequestDto dto = new SignInRequestDto(username, password);

        mockMvc.perform(post("/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ValidateErrorCode.INVALID_DATA.getCode()))
                .andExpect(jsonPath("$.type").value(ValidateErrorCode.INVALID_DATA.getType()))
                .andExpect(jsonPath("$.message").value(ValidateErrorCode.INVALID_DATA.getMessage()))
                .andExpect(content().string(containsString("username: username must not be empty")))
                .andExpect(content().string(containsString("password: password must not be empty"))) ;

        verify(refreshTokenService, never()).saveRefreshToken(any(RefreshToken.class));
    }

    //TODO: implement this...

    @Test
    @DisplayName("로그인 테스트 실패 - 아이디/비밀번호 오류")
    void testSignIn_fail_notExistUser() {
        String username = "test user";
        String password = "password";


    }


}