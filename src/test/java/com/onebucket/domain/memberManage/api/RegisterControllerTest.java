package com.onebucket.domain.memberManage.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onebucket.domain.memberManage.dto.CreateMemberRequestDto;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.RegisterException;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <br>package name   : com.onebucket.domain.memberManage.api
 * <br>file name      : RegisterControllerTest
 * <br>date           : 2024-06-27
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
 * 2024-06-27        jack8              init create
 * </pre>
 */

@ExtendWith(MockitoExtension.class)
class RegisterControllerTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private RegisterController registerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(registerController)
                .setControllerAdvice(new AuthenticationExceptionHandler())
                .setControllerAdvice(new DataExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("회원등록 테스트 성공")
    void testRegister_success() throws Exception {
        //given
        CreateMemberRequestDto dto = CreateMemberRequestDto.builder()
                .username("testuser")
                .password("password")
                .nickname("nickname")
                .build();

        //when & then
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                        .andExpect(status().isOk())
                        .andExpect(result -> assertEquals("success create", result.getResponse().getContentAsString()));

        verify(memberService, times(1)).createMember(any(CreateMemberRequestDto.class));
    }

    @Test
    @DisplayName("회원 등록 실패 - 유효성 검사 실패")
    void testRegister_fail_validation() throws Exception {
        // given
        CreateMemberRequestDto dto = CreateMemberRequestDto.builder()
                .username("")  // invalid username
                .password("")  // invalid password
                .nickname("")  // invalid nickname
                .build();

        // when & then
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ValidateErrorCode.INVALID_DATA.getCode()))
                .andExpect(jsonPath("$.type").value(ValidateErrorCode.INVALID_DATA.getType()))
                .andExpect(jsonPath("$.message").value(ValidateErrorCode.INVALID_DATA.getMessage()))
                .andExpect(content().string(containsString("username: username must not be empty")))
                .andExpect(content().string(containsString("password: password must not be empty")))
                .andExpect(content().string(containsString("nickname: nickname must not be empty")));
        verify(memberService, never()).createMember(any(CreateMemberRequestDto.class));
    }


    @Test
    @DisplayName("회원 등록 실패 - 중복된 값")
    void testRegister_fail_duplicateValue() throws Exception {
        // given
        CreateMemberRequestDto dto = CreateMemberRequestDto.builder()
                .username("testuser")
                .password("password")
                .nickname("duplicatenickname")
                .build();

        doThrow(new RegisterException(AuthenticationErrorCode.DUPLICATE_USER, "username or nickname already exist."))
                .when(memberService).createMember(any(CreateMemberRequestDto.class));

        // when & then
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("1001"))
                .andExpect(jsonPath("$.type").value("AUTH"))
                .andExpect(jsonPath("$.message").value("Already Exist value"));

        verify(memberService, times(1)).createMember(any(CreateMemberRequestDto.class));
    }

}