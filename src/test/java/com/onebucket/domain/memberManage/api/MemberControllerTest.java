package com.onebucket.domain.memberManage.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.onebucket.domain.mailManage.dto.EmailMessage;
import com.onebucket.domain.mailManage.service.MailService;
import com.onebucket.domain.memberManage.dto.*;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.memberManage.service.ProfileService;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.ValidateErrorCode;
import com.onebucket.global.exceptionManage.exceptionHandler.BaseExceptionHandler;
import com.onebucket.global.exceptionManage.exceptionHandler.DataExceptionHandler;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.onebucket.testComponent.testUtils.JsonFieldResultMatcher.hasKey;
import static com.onebucket.testComponent.testUtils.JsonFieldResultMatcher.hasStatus;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <br>package name   : com.onebucket.domain.memberManage.api
 * <br>file name      : MemberControllerTest
 * <br>date           : 2024-07-03
 * <pre>
 * <span style="color: white;">[description]</span>
 * {@link MemberController} 에 대한 테스트 코드이다.
 *
 * "/member/password/reset"
 * "/member/password/set"
 * "/member/nickname/set"
 * "member/{id}/set"
 * "/member/info"
 * "/member"
 * "/profile/update"
 * "/profile/image(POST)"
 * "/profile/basic-image"
 * "/profile/image(GET)"
 * "/profile"
 * </pre>
 */

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @Mock
    private ProfileService profileService;

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private MailService mailService;

    @InjectMocks
    private MemberController memberController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(memberController)
                .setControllerAdvice(new BaseExceptionHandler(), new DataExceptionHandler())
                .build();
    }


    //-+-+-+-+-+-+]] resetPassword test [[-+-+-+-+-+-+
    @Test
    @DisplayName("resetPassword - success")
    void testResetPassword_success() throws Exception {
        Long id = -1L;
        when(securityUtils.getCurrentUsername()).thenReturn("username");
        when(memberService.usernameToId("username")).thenReturn(id);
        when(profileService.readProfile(id)).thenReturn(ReadProfileDto.builder().build());
        mockMvc.perform(post("/member/password/reset")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect((hasKey(new SuccessResponseDto("success reset password"))));

        verify(memberService,  times(1)).changePassword("username");
    }

    @Test
    @DisplayName("resetPassword - fail / not exist authentication in token while getCurrentUsername")
    void testResetPassword_fail_notExistAuth() throws Exception {
        String internalMessage = "Not exist authentication in ContextHolder";
        AuthenticationErrorCode code = AuthenticationErrorCode.NON_EXIST_AUTHENTICATION;
        AuthenticationException exception = new AuthenticationException(code, internalMessage);
        when(securityUtils.getCurrentUsername()).thenThrow(exception);

        mockMvc.perform(post("/member/password/reset")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code, internalMessage));
    }

    @Test
    @DisplayName("resetPassword - fail / unknown username while change password")
    void testResetPassword_fail_unknownUser() throws Exception {
        String username = "username";
        AuthenticationErrorCode code = AuthenticationErrorCode.UNKNOWN_USER;
        AuthenticationException exception = new AuthenticationException(code);
        when(securityUtils.getCurrentUsername()).thenReturn(username);
        when(memberService.changePassword(username)).thenThrow(exception);

        mockMvc.perform(post("/member/password/reset")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));
    }




    //-+-+-+-+-+-+]] setPassword test [[-+-+-+-+-+-+
    @Test
    @DisplayName("setPassword - success")
    void testSetPassword_success() throws Exception {
        String username = "username";
        String password = "!1Password1!";
        SetPasswordDto dto = new SetPasswordDto(password);
        when(securityUtils.getCurrentUsername()).thenReturn(username);
        mockMvc.perform(post("/member/password/set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(hasKey(new SuccessResponseDto("success set password")));

        verify(memberService, times(1)).changePassword(username, password);
    }

    @Test
    @DisplayName("setPassword - fail / not exist authentication in token while getCurrentUsername")
    void testSetPassword_fail_notExistAuth() throws Exception {
        String internalMessage = "Not exist authentication in ContextHolder";
        AuthenticationErrorCode code = AuthenticationErrorCode.NON_EXIST_AUTHENTICATION;
        AuthenticationException exception = new AuthenticationException(code, internalMessage);
        when(securityUtils.getCurrentUsername()).thenThrow(exception);

        SetPasswordDto dto = new SetPasswordDto("!1Password1!");

        mockMvc.perform(post("/member/password/set")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code, internalMessage));
    }

    @Test
    @DisplayName("setPassword - fail / unknown username while change password")
    void testSetPassword_fail_unknownUser() throws Exception {
        String username = "username";
        String password = "!1Password1!";
        SetPasswordDto dto = new SetPasswordDto(password);
        AuthenticationErrorCode code = AuthenticationErrorCode.UNKNOWN_USER;
        AuthenticationException exception = new AuthenticationException(code);
        when(securityUtils.getCurrentUsername()).thenReturn(username);
        when(memberService.changePassword(username, password)).thenThrow(exception);

        mockMvc.perform(post("/member/password/set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));
    }

    @Test
    @DisplayName("setPassword - fail / all validation errors")
    void testSetPassword_fail_allValidationErrors() throws Exception {
        // given
        String password = "";
        SetPasswordDto dto = new SetPasswordDto(password);


        ValidateErrorCode code = ValidateErrorCode.INVALID_DATA;
        // when & then
        mockMvc.perform(post("/member/password/set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code))
                .andExpect(content().string(containsString("password must not be empty")))
                .andExpect(content().string(containsString("password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")))
                .andExpect(content().string(containsString("size of password must be over 8, under 20")));

        verify(memberService, never()).changePassword(anyString(), anyString());
    }


    //-+-+-+-+-+-+]] setNickname test [[-+-+-+-+-+-+
    @Test
    @DisplayName("setNickname - success")
    void testSetNickname_success() throws Exception {
        String username = "username";
        NicknameRequestDto dto = new NicknameRequestDto("nickname");
        when(securityUtils.getCurrentUsername()).thenReturn(username);

        mockMvc.perform(post("/member/nickname/set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(hasKey(new SuccessResponseDto("success set nickname")));

        verify(memberService).updateMember(eq(username), any(NicknameRequestDto.class));
    }

    @Test
    @DisplayName("setNickname - fail / not exist authentication in token while getCurrentUsername")
    void testSetNickname_fail_notExistAuth() throws Exception {
        String internalMessage = "Not exist authentication in ContextHolder";
        AuthenticationErrorCode code = AuthenticationErrorCode.NON_EXIST_AUTHENTICATION;
        AuthenticationException exception = new AuthenticationException(code, internalMessage);
        when(securityUtils.getCurrentUsername()).thenThrow(exception);

        NicknameRequestDto dto = new NicknameRequestDto("nickname");

        mockMvc.perform(post("/member/nickname/set")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code, internalMessage));
    }

    @Test
    @DisplayName("setNickname - fail / unknown username while update Member")
    void testSetNickname_fail_unknownUser() throws Exception {
        String username = "username";
        NicknameRequestDto dto = new NicknameRequestDto("nickname");
        AuthenticationErrorCode code = AuthenticationErrorCode.UNKNOWN_USER;
        AuthenticationException exception = new AuthenticationException(code);
        when(securityUtils.getCurrentUsername()).thenReturn(username);
        doThrow(exception).when(memberService).updateMember(eq(username), any(NicknameRequestDto.class));

        mockMvc.perform(post("/member/nickname/set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));
    }

    @Test
    @DisplayName("setNickname - fail / all validation errors")
    void testSetNickname_fail_allValidationErrors() throws Exception {
        // given
        NicknameRequestDto dto = new NicknameRequestDto();
        dto.setNickname("");  // invalid nickname (empty)

        ValidateErrorCode code = ValidateErrorCode.INVALID_DATA;
        // when & then
        mockMvc.perform(post("/member/nickname/set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code))
                .andExpect(content().string(containsString("nickname: nickname must not be empty")))
                .andExpect(content().string(containsString("nickname: size of nickname must be over 4 under 14")));

        verify(memberService, never()).updateMember(anyString(), any(NicknameRequestDto.class));
    }


    //-+-+-+-+-+-+]] getNickname test [[-+-+-+-+-+-+
    @Test
    @DisplayName("getNickname - success")
    void testGetNickname_success() throws Exception {
        Long memberId = 1L;
        String expectedNickname = "john";

        when(memberService.idToNickname(memberId)).thenReturn(expectedNickname);

        mockMvc.perform(get("/member/{id}/nickname", memberId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(hasKey(new NicknameRequestDto(expectedNickname)));
    }

    @Test
    @DisplayName("getNickname - fail / unknown user while change id to nickname")
    void testGetNickname_fail_unknownUser() throws Exception {
        Long memberId = 1L;
        AuthenticationErrorCode code = AuthenticationErrorCode.UNKNOWN_USER;
        AuthenticationException exception = new AuthenticationException(code);
        when(memberService.idToNickname(memberId)).thenThrow(exception);

        mockMvc.perform(get("/member/{id}/nickname", memberId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));
    }


    //-+-+-+-+-+-+]] getMemberInfo test [[-+-+-+-+-+-+
    @Test
    @DisplayName("getMemberInfo - success")
    void testGetMemberInfo_success() throws Exception {
        ReadMemberInfoDto dto = ReadMemberInfoDto.builder()
                .username("username")
                .nickname("nickname")
                .university("university")
                .build();
        when(securityUtils.getCurrentUsername()).thenReturn("username");
        when(memberService.readMember("username")).thenReturn(dto);

        mockMvc.perform(get("/member/info")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(hasKey(dto));
    }

    @Test
    @DisplayName("getMemberInfo - success / university is null")
    void testGetMemberInfo_success_nullUniv() throws Exception {
        ReadMemberInfoDto dto = ReadMemberInfoDto.builder()
                .username("username")
                .nickname("nickname")
                .university("null") //실제로 member 엔티티에 university 필드가 null 이라면 이를 "null"로 변환해준다.
                .build();

        when(securityUtils.getCurrentUsername()).thenReturn("username");
        when(memberService.readMember("username")).thenReturn(dto);

        mockMvc.perform(get("/member/info")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(hasKey(dto));
    }

    @Test
    @DisplayName("getMemberInfo - fail / not exist authentication in token while getCurrentUsername")
    void testGetMemberInfo_fail_notExistAuth() throws Exception {
        String internalMessage = "Not exist authentication in ContextHolder";
        AuthenticationErrorCode code = AuthenticationErrorCode.NON_EXIST_AUTHENTICATION;
        AuthenticationException exception = new AuthenticationException(code, internalMessage);
        when(securityUtils.getCurrentUsername()).thenThrow(exception);

        mockMvc.perform(get("/member/info")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code, internalMessage));
    }

    @Test
    @DisplayName("getMemberInfo - fail / unknown username while read member info")
    void testGetMemberInfo_fail_unknownUser() throws Exception {
        String username = "username";
        AuthenticationErrorCode code = AuthenticationErrorCode.UNKNOWN_USER;
        AuthenticationException exception = new AuthenticationException(code);
        when(securityUtils.getCurrentUsername()).thenReturn(username);
        when(memberService.readMember(username)).thenThrow(exception);

        mockMvc.perform(get("/member/info")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));
    }


    //-+-+-+-+-+-+]] quitAccount test [[-+-+-+-+-+-+
    @Test
    @DisplayName("quitAccount - success")
    void testQuitAccount_success() throws Exception {
        mockMvc.perform(delete("/member")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(hasKey(new SuccessResponseDto("success delete account")));
    }

    @Test
    @DisplayName("quitAccount - fail / not exist authentication in token while getCurrentUsername")
    void testQuitAccount_fail_notExistAuth() throws Exception {
        String internalMessage = "Not exist authentication in ContextHolder";
        AuthenticationErrorCode code = AuthenticationErrorCode.NON_EXIST_AUTHENTICATION;
        AuthenticationException exception = new AuthenticationException(code, internalMessage);
        when(securityUtils.getCurrentUsername()).thenThrow(exception);

        mockMvc.perform(delete("/member")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code, internalMessage));
    }

    @Test
    @DisplayName("quitAccount - fail / can't find username while quit member")
    void testQuitAccount_fail_unknownUser() throws Exception {
        String username = "username";
        AuthenticationErrorCode code = AuthenticationErrorCode.UNKNOWN_USER;
        AuthenticationException exception = new AuthenticationException(code);
        when(securityUtils.getCurrentUsername()).thenReturn(username);
        doThrow(exception).when(memberService).quitMember(username);

        mockMvc.perform(delete("/member")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));
    }


    //-+-+-+-+-+-+]] updateProfile test [[-+-+-+-+-+-+
    @Test
    @DisplayName("updateProfile - success")
    void testUpdateProfile_success() throws Exception {
        //given
        UpdateProfileDto dto = UpdateProfileDto.builder()
                .name("사용자")
                .gender("man")
                .age(20)
                .birth(LocalDate.of(1999, 8, 20))
                .description("test description")
                .build();

        when(securityUtils.getCurrentUsername()).thenReturn("username");
        when(memberService.usernameToId("username")).thenReturn(1L);

        mockMvc.perform(post("/profile/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(hasKey(new SuccessResponseDto("success update profile")));

        verify(profileService, times(1)).updateProfile(eq(1L), any(UpdateProfileDto.class));

    }

    @Test
    @DisplayName("updateProfile - fail / not exist authentication in token while getCurrentUsername")
    void testUpdateProfile_fail_notExistAuth() throws Exception {
        //given
        UpdateProfileDto dto = UpdateProfileDto.builder()
                .name("사용자")
                .gender("man")
                .age(20)
                .birth(LocalDate.of(1999,8,20))
                .description("test description")
                .build();
        String internalMessage = "Not exist Authentication in ContextHolder";
        AuthenticationErrorCode code = AuthenticationErrorCode.NON_EXIST_AUTHENTICATION;
        AuthenticationException exception = new AuthenticationException(code, internalMessage);
        when(securityUtils.getCurrentUsername()).thenThrow(exception);

        mockMvc.perform(post("/profile/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code, internalMessage));

        verify(profileService, never()).updateProfile(anyLong(), any(UpdateProfileDto.class));
    }

    @Test
    @DisplayName("updateProfile - fail / can't find user while username to id")
    void testUpdateProfile_fail_unknownUser() throws Exception {
        String username = "username";
        UpdateProfileDto dto = UpdateProfileDto.builder()
                .name("사용자")
                .gender("man")
                .age(20)
                .birth(LocalDate.of(1999,8,20))
                .description("test description")
                .build();
        AuthenticationErrorCode code = AuthenticationErrorCode.UNKNOWN_USER;
        AuthenticationException exception = new AuthenticationException(code);
        when(securityUtils.getCurrentUsername()).thenReturn(username);
        when(memberService.usernameToId(username)).thenThrow(exception);

        mockMvc.perform(post("/profile/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));

        verify(profileService, never()).updateProfile(anyLong(), any(UpdateProfileDto.class));
    }

    @Test
    @DisplayName("updateProfile - fail / can't find profile while update profile")
    void testUpdateProfile_fail_unknownProfile() throws Exception {
        //given
        String username = "username";
        UpdateProfileDto dto = UpdateProfileDto.builder()
                .name("사용자")
                .gender("man")
                .age(20)
                .birth(LocalDate.of(1999,8,20))
                .description("test description")
                .build();
        AuthenticationErrorCode code = AuthenticationErrorCode.UNKNOWN_USER_PROFILE;
        AuthenticationException exception = new AuthenticationException(code);
        when(securityUtils.getCurrentUsername()).thenReturn(username);
        when(memberService.usernameToId(username)).thenReturn(1L);
        doThrow(exception).when(profileService).updateProfile(eq(1L), any(UpdateProfileDto.class));

        mockMvc.perform(post("/profile/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));

        verify(profileService, times(1)).updateProfile(anyLong(), any(UpdateProfileDto.class));
    }

    @Test
    @DisplayName("updateProfile - fail / all validation errors")
    void testUpdateProfile_fail_allValidationErrors() throws Exception {
        // given
        UpdateProfileDto dto = UpdateProfileDto.builder()
                .name("이 한글로 된 이름은 너무 긴 나머지 예외를 일으킨다.")
                .gender("invalid_gender")
                .age(151) // invalid age (exceeds maximum)
                .description("This description is way too long and should trigger a validation error because it exceeds the maximum allowed length of 200 characters. " +
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus imperdiet, nulla et dictum interdum, nisi lorem egestas odio, vitae scelerisque enim ligula venenatis dolor.")
                .birth(LocalDate.now().plusDays(1)) // invalid date (not in the past)
                .build();

        ValidateErrorCode code = ValidateErrorCode.INVALID_DATA;
        // when & then
        mockMvc.perform(post("/profile/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code))
                .andExpect(content().string(containsString("name: size of name must be over 2 under 5")))
                .andExpect(content().string(containsString("gender: Gender must be either 'man' or 'woman'")))
                .andExpect(content().string(containsString("age: Age must be at most 110")))
                .andExpect(content().string(containsString("description: Description must be at most 200 characters long")))
                .andExpect(content().string(containsString("birth: Birth date must be in the past")));

        verify(profileService, never()).updateProfile(anyLong(), any(UpdateProfileDto.class));
    }


    //-+-+-+-+-+-+]] updateImage test [[-+-+-+-+-+-+
    @Test
    @DisplayName("updateImage - success")
    void testUpdateImage_success() throws Exception {
        //given
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                "test image content".getBytes()
        );

        when(securityUtils.getCurrentUsername()).thenReturn("username");
        when(memberService.usernameToId("username")).thenReturn(1L);

        mockMvc.perform(multipart("/profile/image")
                        .file(mockFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(hasKey(new SuccessResponseDto("success update image")));

        verify(profileService, times(1)).updateImage(1L, mockFile);
    }

    @Test
    @DisplayName("updateImage - fail / not exist authentication in token while getCurrentUsername")
    void testUpdateImage_fail_notExistAuth() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                "test image content".getBytes()
        );

        String internalMessage = "Not exist Authentication in ContextHolder";
        AuthenticationErrorCode code = AuthenticationErrorCode.NON_EXIST_AUTHENTICATION;
        AuthenticationException exception = new AuthenticationException(code, internalMessage);
        when(securityUtils.getCurrentUsername()).thenThrow(exception);

        mockMvc.perform(multipart("/profile/image")
                        .file(mockFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code, internalMessage));
    }

    @Test
    @DisplayName("updateImage - fail / unknown username while fetching user id")
    void testUpdateImage_fail_unknownUser() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                "test image content".getBytes()
        );

        String username = "username";
        AuthenticationErrorCode code = AuthenticationErrorCode.UNKNOWN_USER;
        AuthenticationException exception = new AuthenticationException(code);
        when(securityUtils.getCurrentUsername()).thenReturn(username);
        when(memberService.usernameToId(username)).thenThrow(exception);

        mockMvc.perform(multipart("/profile/image")
                        .file(mockFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));
    }

    @Test
    @DisplayName("updateImage - fail / can't find profile while updating image")
    void testUpdateImage_fail_unknownProfile() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                "test image content".getBytes()
        );

        String username = "username";
        AuthenticationErrorCode code = AuthenticationErrorCode.UNKNOWN_USER_PROFILE;
        AuthenticationException exception = new AuthenticationException(code);
        when(securityUtils.getCurrentUsername()).thenReturn(username);
        when(memberService.usernameToId(username)).thenReturn(1L);
        doThrow(exception).when(profileService).updateImage(eq(1L), any(MockMultipartFile.class));

        mockMvc.perform(multipart("/profile/image")
                        .file(mockFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));
    }


    //-+-+-+-+-+-+]] updateToBasicImage test [[-+-+-+-+-+-+
    @Test
    @DisplayName("updateToBasicImage - success")
    void testUpdateToBasicImage_success() throws Exception {
        String username = "username";

        when(securityUtils.getCurrentUsername()).thenReturn(username);
        when(memberService.usernameToId(username)).thenReturn(1L);

        mockMvc.perform(post("/profile/basic-image")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(hasKey(new SuccessResponseDto("success update basic image")));

        verify(profileService, times(1)).updateImageToBasic(1L);
    }

    @Test
    @DisplayName("updateToBasicImage - fail / not exist authentication in token while getCurrentUsername")
    void testUpdateToBasicImage_fail_notExistAuth() throws Exception {
        String internalMessage = "Not exist Authentication in ContextHolder";
        AuthenticationErrorCode code = AuthenticationErrorCode.NON_EXIST_AUTHENTICATION;
        AuthenticationException exception = new AuthenticationException(code, internalMessage);
        when(securityUtils.getCurrentUsername()).thenThrow(exception);

        mockMvc.perform(post("/profile/basic-image")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code, internalMessage));
    }

    @Test
    @DisplayName("updateToBasicImage - fail / unknown username while fetching user id")
    void testUpdateToBasicImage_fail_unknownUser() throws Exception {
        String username = "username";
        AuthenticationErrorCode code = AuthenticationErrorCode.UNKNOWN_USER;
        AuthenticationException exception = new AuthenticationException(code);
        when(securityUtils.getCurrentUsername()).thenReturn(username);
        when(memberService.usernameToId(username)).thenThrow(exception);

        mockMvc.perform(post("/profile/basic-image")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));
    }

    @Test
    @DisplayName("updateToBasicImage - fail / can't find profile while updating to basic image")
    void testUpdateToBasicImage_fail_unknownProfile() throws Exception {
        String username = "username";
        AuthenticationErrorCode code = AuthenticationErrorCode.UNKNOWN_USER_PROFILE;
        AuthenticationException exception = new AuthenticationException(code);
        when(securityUtils.getCurrentUsername()).thenReturn(username);
        when(memberService.usernameToId(username)).thenReturn(1L);
        doThrow(exception).when(profileService).updateImageToBasic(1L);

        mockMvc.perform(post("/profile/basic-image")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));
    }


    //-+-+-+-+-+-+]] getImage test [[-+-+-+-+-+-+
    @Test
    @DisplayName("getImage - success")
    void testGetImage_success() throws Exception {
        //given
        byte[] imageBytes = "test image content".getBytes();

        when(securityUtils.getCurrentUsername()).thenReturn("username");
        when(memberService.usernameToId("username")).thenReturn(1L);
        when(profileService.readProfileImage(1L)).thenReturn(imageBytes);

        mockMvc.perform(get("/profile/image")
                        .accept(MediaType.IMAGE_PNG_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("content-Type", MediaType.IMAGE_PNG_VALUE))
                .andExpect(content().bytes(imageBytes));
    }

    @Test
    @DisplayName("getImage - fail / not exist authentication in token while getCurrentUsername")
    void testGetImage_fail_notExistAuth() throws Exception {
        String internalMessage = "Not exist Authentication in ContextHolder";
        AuthenticationErrorCode code = AuthenticationErrorCode.NON_EXIST_AUTHENTICATION;
        AuthenticationException exception = new AuthenticationException(code, internalMessage);
        when(securityUtils.getCurrentUsername()).thenThrow(exception);

        mockMvc.perform(get("/profile/image")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code, internalMessage));
    }

    @Test
    @DisplayName("getImage - fail / unknown username while fetching user id")
    void testGetImage_fail_unknownUser() throws Exception {
        String username = "username";
        AuthenticationErrorCode code = AuthenticationErrorCode.UNKNOWN_USER;
        AuthenticationException exception = new AuthenticationException(code);
        when(securityUtils.getCurrentUsername()).thenReturn(username);
        when(memberService.usernameToId(username)).thenThrow(exception);

        mockMvc.perform(get("/profile/image")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));
    }

    @Test
    @DisplayName("getImage - fail / can't find profile while fetching image")
    void testGetImage_fail_unknownProfile() throws Exception {
        String username = "username";
        AuthenticationErrorCode code = AuthenticationErrorCode.UNKNOWN_USER_PROFILE;
        AuthenticationException exception = new AuthenticationException(code);
        when(securityUtils.getCurrentUsername()).thenReturn(username);
        when(memberService.usernameToId(username)).thenReturn(1L);
        doThrow(exception).when(profileService).readProfileImage(1L);

        mockMvc.perform(get("/profile/image")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));
    }

    @Test
    @DisplayName("getImage - fail / error reading profile image")
    void testGetImage_fail_readImageError() throws Exception {
        String username = "username";
        AuthenticationErrorCode code = AuthenticationErrorCode.PROFILE_IMAGE_ERROR;
        AuthenticationException exception = new AuthenticationException(code);
        when(securityUtils.getCurrentUsername()).thenReturn(username);
        when(memberService.usernameToId(username)).thenReturn(1L);
        when(profileService.readProfileImage(1L)).thenThrow(exception);

        mockMvc.perform(get("/profile/image")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));
    }


    //-+-+-+-+-+-+]] getProfile test [[-+-+-+-+-+-+
    @Test
    @DisplayName("getProfile - success")
    void testGetProfile_success() throws Exception {
        ReadProfileDto dto = ReadProfileDto.builder()
                .name("test user")
                .gender("man")
                .age(20)
                .description("test description")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .birth(LocalDate.now())
                .build();

        when(securityUtils.getCurrentUsername()).thenReturn("username");
                when(memberService.usernameToId("username")).thenReturn(1L);
        when(profileService.readProfile(1L)).thenReturn(dto);

        mockMvc.perform(get("/profile")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(hasKey(dto));
    }

    @Test
    @DisplayName("getProfile - fail / not exist authentication in token while getCurrentUsername")
    void testGetProfile_fail_notExistAuth() throws Exception {
        String internalMessage = "Not exist Authentication in ContextHolder";
        AuthenticationErrorCode code = AuthenticationErrorCode.NON_EXIST_AUTHENTICATION;
        AuthenticationException exception = new AuthenticationException(code, internalMessage);
        when(securityUtils.getCurrentUsername()).thenThrow(exception);

        mockMvc.perform(get("/profile")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code, internalMessage));
    }

    @Test
    @DisplayName("getProfile - fail / unknown username while fetching user id")
    void testGetProfile_fail_unknownUser() throws Exception {
        String username = "username";
        AuthenticationErrorCode code = AuthenticationErrorCode.UNKNOWN_USER;
        AuthenticationException exception = new AuthenticationException(code);
        when(securityUtils.getCurrentUsername()).thenReturn(username);
        when(memberService.usernameToId(username)).thenThrow(exception);

        mockMvc.perform(get("/profile")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));
    }

    @Test
    @DisplayName("getProfile - fail / can't find profile")
    void testGetProfile_fail_unknownProfile() throws Exception {
        String username = "username";
        AuthenticationErrorCode code = AuthenticationErrorCode.UNKNOWN_USER_PROFILE;
        AuthenticationException exception = new AuthenticationException(code);
        when(securityUtils.getCurrentUsername()).thenReturn(username);
        when(memberService.usernameToId(username)).thenReturn(1L);
        when(profileService.readProfile(1L)).thenThrow(exception);

        mockMvc.perform(get("/profile")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(hasStatus(code))
                .andExpect(hasKey(code));
    }
}
