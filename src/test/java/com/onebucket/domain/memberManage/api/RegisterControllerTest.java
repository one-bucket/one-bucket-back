package com.onebucket.domain.memberManage.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.onebucket.domain.memberManage.dto.CreateMemberRequestDto;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.memberManage.service.ProfileService;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.ValidateErrorCode;
import com.onebucket.global.exceptionManage.exceptionHandler.BaseExceptionHandler;
import com.onebucket.global.exceptionManage.exceptionHandler.DataExceptionHandler;
import com.onebucket.global.utils.SuccessResponseDto;
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

import java.nio.charset.StandardCharsets;

import static com.onebucket.testComponent.testUtils.JsonFieldResultMatcher.hasKey;
import static com.onebucket.testComponent.testUtils.JsonFieldResultMatcher.hasStatus;
import static org.hamcrest.Matchers.containsString;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <br>package name   : com.onebucket.domain.memberManage.api
 * <br>file name      : RegisterControllerTest
 * <br>date           : 2024-06-27
 * <pre>
 * <span style="color: white;">[description]</span>
 * {@link RegisterController} 에 대한 테스트 코드이다.
 * </pre>
 */

@ExtendWith(MockitoExtension.class)
class RegisterControllerTest {

    @Mock
    private MemberService memberService;


    @Mock
    private ProfileService profileService;

    @InjectMocks
    private RegisterController registerController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(registerController)
                .setControllerAdvice(new BaseExceptionHandler(), new DataExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("register - success")
    void testRegister_success() throws Exception {
        //given
        CreateMemberRequestDto dto = CreateMemberRequestDto.builder()
                .username("username")
                .password("!1Password1!")
                .nickname("nickname")
                .build();

        //when & then
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(hasKey(new SuccessResponseDto("success register")));

        verify(memberService, times(1)).createMember(any(CreateMemberRequestDto.class));
    }

    @Test
    @DisplayName("register - fail / empty value insert")
    void testRegister_fail_validation() throws Exception {
        // given
        CreateMemberRequestDto dto = CreateMemberRequestDto.builder()
                .username("")  // invalid username
                .password("")  // invalid password
                .nickname("")  // invalid nickname
                .build();

        ValidateErrorCode code = ValidateErrorCode.INVALID_DATA;
        // when & then
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code))
                .andExpect(content().string(containsString("username: username must not be empty")))
                .andExpect(content().string(containsString("password: password must not be empty")))
                .andExpect(content().string(containsString("nickname: nickname must not be empty")))
                .andExpect(content().string(containsString("password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")));
        verify(memberService, never()).createMember(any(CreateMemberRequestDto.class));
    }


    @Test
    @DisplayName("회원 등록 실패 - 중복된 값")
    void testRegister_fail_duplicateValue() throws Exception {
        // given
        CreateMemberRequestDto dto = CreateMemberRequestDto.builder()
                .username("username")
                .password("!1Password1!")
                .nickname("duplicateName")
                .build();

        AuthenticationErrorCode code = AuthenticationErrorCode.DUPLICATE_USER;
        String internalMessage = "username or nickname already exist.";
        doThrow(new AuthenticationException(code, internalMessage))
                .when(memberService).createMember(any(CreateMemberRequestDto.class));

        // when & then
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code, internalMessage));

        verify(memberService, times(1)).createMember(any(CreateMemberRequestDto.class));
    }

    @Test
    @DisplayName("회원 등록 실패 - 프로필 생성 오류")
    void testRegister_fail_duplicateProfile() throws Exception {
        //given
        CreateMemberRequestDto dto = CreateMemberRequestDto.builder()
                .username("username")
                .password("!1Password1!")
                .nickname("nickname")
                .build();
        AuthenticationErrorCode code = AuthenticationErrorCode.DUPLICATE_PROFILE;

        when(memberService.createMember(any(CreateMemberRequestDto.class))).thenReturn(1L);
        doThrow(new AuthenticationException(code))
                .when(profileService).createInitProfile(1L);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));

    }

}