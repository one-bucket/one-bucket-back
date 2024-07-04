package com.onebucket.domain.memberManage.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.onebucket.domain.memberManage.dto.ReadProfileDto;
import com.onebucket.domain.memberManage.dto.UpdateProfileDto;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.memberManage.service.ProfileService;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.exceptionHandler.AuthenticationExceptionHandler;
import com.onebucket.global.utils.SecurityUtils;
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

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.onebucket.testComponent.JsonFieldResultMatcher.hasKey;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <br>package name   : com.onebucket.domain.memberManage.api
 * <br>file name      : MemberControllerTest
 * <br>date           : 2024-07-03
 * <pre>
 * <span style="color: white;">[description]</span>
 * {@link MemberController} 에 대한 테스트 코드이다.
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

    @InjectMocks
    private MemberController memberController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(memberController)
                .setControllerAdvice(new AuthenticationExceptionHandler())
                .build();
    }

    //-+-+-+-+-+-+]] updateProfile test [[-+-+-+-+-+-+
    @Test
    @DisplayName("정상적인 프로필 갱신")
    void testUpdateProfile_success() throws Exception {
        //given
        UpdateProfileDto dto = UpdateProfileDto.builder()
                .name("test user")
                .gender("man")
                .age(20)
                .birth(LocalDate.of(1999,8,20))
                .description("test description")
                .build();

        when(securityUtils.getCurrentUsername()).thenReturn("username");
        when(memberService.usernameToId("username")).thenReturn(1L);

        mockMvc.perform(post("/profile/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(result ->
                        assertEquals("success update profile", result.getResponse().getContentAsString()));

        verify(profileService, times(1)).updateProfile(eq(1L), any(UpdateProfileDto.class));

    }

    @Test
    @DisplayName("비정상적인 프로필 갱신 - 유저 찾을 수 없음")
    void testUpdateProfile_fail_unknownUser() throws Exception {
        //given
        UpdateProfileDto dto = UpdateProfileDto.builder()
                .name("test user")
                .gender("man")
                .age(20)
                .birth(LocalDate.of(1999,8,20))
                .description("test description")
                .build();

        AuthenticationException exception = new AuthenticationException(AuthenticationErrorCode.NON_EXIST_AUTHENTICATION,
                "Not exist Authentication in ContextHolder");
        when(securityUtils.getCurrentUsername()).thenThrow(exception);

        mockMvc.perform(post("/profile/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpect(hasKey(AuthenticationErrorCode.NON_EXIST_AUTHENTICATION, "Not exist Authentication in ContextHolder"));


        verify(profileService, never()).updateProfile(anyLong(), any(UpdateProfileDto.class));
    }

    @Test
    @DisplayName("비정상적인 프로필 갱신 - 프로필을 찾을 수 없음")
    void testUpdateProfile_fail_unknownProfile() throws Exception {
        //given
        UpdateProfileDto dto = UpdateProfileDto.builder()
                .name("test user")
                .gender("man")
                .age(20)
                .birth(LocalDate.of(1999,8,20))
                .description("test description")
                .build();

        when(securityUtils.getCurrentUsername()).thenReturn("username");
        AuthenticationException exception = new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER_PROFILE);
        when(memberService.usernameToId("username")).thenReturn(1L);
        doThrow(exception).when(profileService).updateProfile(eq(1L), any(UpdateProfileDto.class));

        mockMvc.perform(post("/profile/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(hasKey(AuthenticationErrorCode.UNKNOWN_USER_PROFILE));

        verify(profileService, times(1)).updateProfile(anyLong(), any(UpdateProfileDto.class));
    }

    //-+-+-+-+-+-+]] updateImage test [[-+-+-+-+-+-+
    //예외 상황(securityUtils.getCurrentUsername(), memberService.usernameToId())에 대해선 기존 메서드에서
    //테스트를 완료 했으므로 넘어감. 또한 updateProfile 과 updateImage 의 경우 특정 예외 가령, profile 을 불러오는 과정은
    //객체 내 동일한 메서드를 의존하므로 이 역시도 생략함.

    @Test
    @DisplayName("정상적인 이미지 업데이트")
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
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(result ->
                        assertEquals("success update image", result.getResponse().getContentAsString()));

        verify(profileService, times(1)).updateImage(1L, mockFile);

    }

    //-+-+-+-+-+-+]] updateToBasicImage test [[-+-+-+-+-+-+
    //예외 상황(securityUtils.getCurrentUsername(), memberService.usernameToId())에 대해선 기존 메서드에서
    //테스트를 완료 했으므로 넘어감. 또한 updateProfile 과 updateImageToBasic 의 경우 특정 예외 가령, profile 을 불러오는 과정은
    //객체 내 동일한 메서드를 의존하므로 이 역시도 생략함.
    //해당 메서드의 경우 절차가 updateProfile 과 동일하므로 이 역시도 생략함.

    //-+-+-+-+-+-+]] getImage test [[-+-+-+-+-+-+
    @Test
    @DisplayName("정상적인 이미지 반환")
    void testGetImage_success() throws Exception {
        //given
        byte[] imageBytes = "test image content".getBytes();

        when(securityUtils.getCurrentUsername()).thenReturn("username");
        when(memberService.usernameToId("username")).thenReturn(1L);
        when(profileService.readProfileImage(1L)).thenReturn(imageBytes);
        mockMvc.perform(get("/profile/image"))
                .andExpect(status().isOk())
                .andExpect(header().string("content-Type", MediaType.IMAGE_PNG_VALUE))
                .andExpect(content().bytes(imageBytes));

    }

    @Test
    @DisplayName("비정상적인 이미지 변환 - minio 관련 예외")
    void testGetImage_fail_minioError() throws Exception {
        when(securityUtils.getCurrentUsername()).thenReturn("username");
        when(memberService.usernameToId("username")).thenReturn(1L);
        AuthenticationException exception = new AuthenticationException(AuthenticationErrorCode.PROFILE_IMAGE_ERROR);
        when(profileService.readProfileImage(1L)).thenThrow(exception);

        mockMvc.perform(get("/profile/image"))
                .andExpect(status().isNotFound())
                .andExpect(hasKey(AuthenticationErrorCode.PROFILE_IMAGE_ERROR));

    }

    //-+-+-+-+-+-+]] getProfile test [[-+-+-+-+-+-+
    @Test
    @DisplayName("정상적인 프로필 호출")
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
    @DisplayName("비정상적인 프로필 호출 - 프로필 없음")
    void testProfile_fail_unknownProfile() throws Exception {
        when(securityUtils.getCurrentUsername()).thenReturn("username");
        when(memberService.usernameToId("username")).thenReturn(1L);
        AuthenticationException exception = new AuthenticationException(AuthenticationErrorCode.UNKNOWN_USER_PROFILE);
        when(profileService.readProfile(1L)).thenThrow(exception);

        mockMvc.perform(get("/profile/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(hasKey(AuthenticationErrorCode.UNKNOWN_USER_PROFILE));
    }








}