package com.onebucket.domain.memberManage.service;

import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.dao.ProfileRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.memberManage.domain.Profile;
import com.onebucket.domain.memberManage.dto.ReadProfileDto;
import com.onebucket.domain.memberManage.dto.UpdateProfileDto;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.minio.MinioRepository;
import com.onebucket.global.minio.MinioInfoDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * <br>package name   : com.onebucket.domain.memberManage.service
 * <br>file name      : ProfileServiceTest
 * <br>date           : 2024-07-06
 * <pre>
 * <span style="color: white;">[description]</span>
 * test of {@link ProfileService}.
 * </pre>
 */

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MinioRepository minioRepository;

    @Mock
    private Profile mockProfile;

    @Mock
    private Member mockMember;

    @InjectMocks
    private ProfileServiceImpl profileService;

    //-+-+-+-+-+-+]] createInitProfile test [[-+-+-+-+-+-+
    @Test
    @DisplayName("createInitProfile - success")
    void testCreateInitProfile_success() {

        Long id = 1L;
        when(memberRepository.findById(id)).thenReturn(Optional.of(mockMember));

        profileService.createInitProfile(id);

        verify(memberRepository, times(1)).save(mockMember);
    }

    @Test
    @DisplayName("createInitProfile - fail / already exist profile")
    void testCreateInitProfile_fail_alreadyExistProfile() {
        Long id = 1L;

        doThrow(DataIntegrityViolationException.class).when(profileRepository).save(any(Profile.class));

        assertThatThrownBy(() -> profileService.createInitProfile(id))
                .isInstanceOf(AuthenticationException.class)
                .extracting("errorCode")
                .isEqualTo(AuthenticationErrorCode.DUPLICATE_PROFILE);

        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    @DisplayName("createInitProfile - fail / can't find member")
    void testCreateInitProfile_fail_unknownUser() {
        Long id = 1L;
        when(memberRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> profileService.createInitProfile(id))
                .isInstanceOf(AuthenticationException.class)
                .extracting("errorCode")
                .isEqualTo(AuthenticationErrorCode.UNKNOWN_USER);

        verify(memberRepository, never()).save(any(Member.class));
    }


    //-+-+-+-+-+-+]] readProfile test [[-+-+-+-+-+-+
    @Test
    @DisplayName("readProfile - success")
    void testReadProfile_success() {
        Long id = 1L;
        when(profileRepository.findById(id)).thenReturn(Optional.of(mockProfile));

        when(mockProfile.getName()).thenReturn("name");
        when(mockProfile.getBirth()).thenReturn(LocalDate.of(1999, 8, 20));
        when(mockProfile.getDescription()).thenReturn("description");
        when(mockProfile.getAge()).thenReturn(26);
        when(mockProfile.getGender()).thenReturn("man");
        when(mockProfile.getCreateAt()).thenReturn(LocalDateTime.of(2024, 5, 21, 13, 10));
        when(mockProfile.getUpdateAt()).thenReturn(LocalDateTime.of(2024, 7, 21, 13, 10));

        ReadProfileDto result = profileService.readProfile(id);

        assertThat(result.getName()).isEqualTo("name");
        assertThat(result.getBirth()).isEqualTo(LocalDate.of(1999, 8, 20));
        assertThat(result.getDescription()).isEqualTo("description");
        assertThat(result.getAge()).isEqualTo(26);
    }

    @Test
    @DisplayName("readProfile - fail / unknown profile")
    void testReadProfile_fail_unknownProfile() {
        Long id = 1L;
        when(profileRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> profileService.readProfile(id))
                .isInstanceOf(AuthenticationException.class)
                .extracting("errorCode")
                .isEqualTo(AuthenticationErrorCode.UNKNOWN_USER_PROFILE);
    }

    //-+-+-+-+-+-+]] readProfileImage test [[-+-+-+-+-+-+
    @Test
    @DisplayName("readProfileImage - success / basic image")
    void testReadProfileImage_success_basicImg() {
        Long id = 1L;
        when(profileRepository.findById(id)).thenReturn(Optional.of(mockProfile));
        when(mockProfile.isBasicImage()).thenReturn(true);

        byte[] expectedBytes = new byte[]{1, 2, 3};
        when(minioRepository.getFile(any(MinioInfoDto.class))).thenReturn(expectedBytes);

        byte[] result = profileService.readProfileImage(id);

        //then
        ArgumentCaptor<MinioInfoDto> captor = ArgumentCaptor.forClass(MinioInfoDto.class);
        verify(minioRepository).getFile(captor.capture());
        MinioInfoDto captureDto = captor.getValue();

        assertThat(captureDto).isNotNull();
        assertThat(captureDto.getFileName()).isEqualTo("/profile/basic_profile_image");
        assertThat(captureDto.getFileExtension()).isEqualTo("png");
        assertThat(expectedBytes).isEqualTo(result);
    }

    @Test
    @DisplayName("readProfileImage - success / user image")
    void testReadProfileImage_success_userImg() {
        Long id = 1L;
        when(profileRepository.findById(id)).thenReturn(Optional.of(mockProfile));
        when(mockProfile.isBasicImage()).thenReturn(false);

        byte[] expectedBytes = new byte[]{1, 2, 3};
        when(minioRepository.getFile(any(MinioInfoDto.class))).thenReturn(expectedBytes);

        byte[] result = profileService.readProfileImage(id);

        //then
        ArgumentCaptor<MinioInfoDto> captor = ArgumentCaptor.forClass(MinioInfoDto.class);
        verify(minioRepository).getFile(captor.capture());
        MinioInfoDto captureDto = captor.getValue();

        assertThat(captureDto).isNotNull();
        assertThat(captureDto.getFileName()).isEqualTo("/profile/" + id + "/profile_image");
        assertThat(captureDto.getFileExtension()).isEqualTo("png");
        assertThat(expectedBytes).isEqualTo(result);
    }

    @Test
    @DisplayName("readProfileImage - fail / minio exception")
    void testReadProfileImage_fail_minioError() {
        Long id = 1L;
        when(profileRepository.findById(id)).thenReturn(Optional.of(mockProfile));
        when(mockProfile.isBasicImage()).thenReturn(false);
        when(minioRepository.getFile(any(MinioInfoDto.class))).thenThrow(new RuntimeException("error occur while fetching file"));

        assertThatThrownBy(() -> profileService.readProfileImage(id))
                .isInstanceOf(AuthenticationException.class)
                .extracting("errorCode")
                .isEqualTo(AuthenticationErrorCode.PROFILE_IMAGE_ERROR);
    }

    //-+-+-+-+-+-+]] updateProfile test [[-+-+-+-+-+-+
    @Test
    @DisplayName("updateProfile - success / all value change")
    void testUpdateProfile_success_allChange() {
        Long id = 1L;
        UpdateProfileDto dto = UpdateProfileDto.builder()
                .name("name")
                .gender("man")
                .age(26)
                .description("description")
                .birth(LocalDate.of(1999, 8, 20))
                .build();

        Profile profile = Profile.builder()
                .name("old name")
                .gender("woman")
                .age(20)
                .description("old description")
                .birth(LocalDate.of(1998, 1, 1))
                .build();

        when(profileRepository.findById(id)).thenReturn(Optional.of(profile));

        profileService.updateProfile(id, dto);

        ArgumentCaptor<Profile> captor = ArgumentCaptor.forClass(Profile.class);
        verify(profileRepository).save(captor.capture());
        Profile captureProfile = captor.getValue();

        assertThat(captureProfile.getName()).isEqualTo("name");
        assertThat(captureProfile.getGender()).isEqualTo("man");
        assertThat(captureProfile.getAge()).isEqualTo(26);
        assertThat(captureProfile.getDescription()).isEqualTo("description");
        assertThat(captureProfile.getBirth()).isEqualTo(LocalDate.of(1999, 8, 20));
    }

    @Test
    @DisplayName("updateProfile - success / some value change")
    void testUpdateProfile_success_someChange() {
        Long id = 1L;
        UpdateProfileDto dto = UpdateProfileDto.builder()
                .name("name")
                .age(26)
                .build();

        Profile profile = Profile.builder()
                .name("old name")
                .gender("woman")
                .age(20)
                .description("old description")
                .birth(LocalDate.of(1998, 1, 1))
                .build();

        when(profileRepository.findById(id)).thenReturn(Optional.of(profile));

        profileService.updateProfile(id, dto);

        ArgumentCaptor<Profile> captor = ArgumentCaptor.forClass(Profile.class);
        verify(profileRepository).save(captor.capture());
        Profile captureProfile = captor.getValue();

        assertThat(captureProfile.getName()).isEqualTo("name");
        assertThat(captureProfile.getGender()).isEqualTo("woman");
        assertThat(captureProfile.getAge()).isEqualTo(26);
        assertThat(captureProfile.getDescription()).isEqualTo("old description");
        assertThat(captureProfile.getBirth()).isEqualTo(LocalDate.of(1998, 1, 1));
    }

    //-+-+-+-+-+-+]] updateProfileImage test [[-+-+-+-+-+-+
    @Test
    @DisplayName("updateImage - success")
    void testUpdateImage_success() {
        Long id = 1L;
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                "test image content".getBytes()
        );

        when(profileRepository.findById(id)).thenReturn(Optional.of(mockProfile));

        profileService.updateImage(id, mockFile);

        ArgumentCaptor<Profile> captor = ArgumentCaptor.forClass(Profile.class);
        verify(profileRepository).save(captor.capture());
        Profile captureProfile = captor.getValue();

        assertThat(captureProfile).isNotNull();
        assertThat(captureProfile.isBasicImage()).isFalse();
    }

    @Test
    @DisplayName("updateImage - fail / unknown profile")
    void testUpdateImage_fail_unknownProfile() {
        Long id = 1L;
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                "test image content".getBytes()
        );

        when(profileRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> profileService.updateImage(id, mockFile))
                .isInstanceOf(AuthenticationException.class)
                .extracting("errorCode")
                .isEqualTo(AuthenticationErrorCode.UNKNOWN_USER_PROFILE);
    }

    @Test
    @DisplayName("updateImage - fail / image save fail in minio")
    void testUpdateImage_fail_minioSaveError() {
        Long id = 1L;
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                "test image content".getBytes()
        );

        when(profileRepository.findById(id)).thenReturn(Optional.of(mockProfile));
        doThrow(new RuntimeException("error occur : message")).when(minioRepository)
                .uploadFile(any(MultipartFile.class), any(MinioInfoDto.class));

        assertThatThrownBy(() -> profileService.updateImage(id, mockFile))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining("error occur")
                .extracting("errorCode")
                .isEqualTo(AuthenticationErrorCode.PROFILE_IMAGE_ERROR);
    }


    //-+-+-+-+-+-+]] updateImageToBasic test [[-+-+-+-+-+-+
    @Test
    @DisplayName("updateImageToBasic - success")
    void testUpdateImageToBasic_success() {
        Long id = 1L;
        when(profileRepository.findById(id)).thenReturn(Optional.of(mockProfile));

        profileService.updateImageToBasic(id);

        ArgumentCaptor<Profile> captor = ArgumentCaptor.forClass(Profile.class);
        verify(profileRepository).save(captor.capture());
        Profile captureProfile = captor.getValue();

        assertThat(captureProfile.isBasicImage()).isFalse();
    }

    @Test
    @DisplayName("updateImageToBasic - fail / unknown profile")
    void testUpdateImageToBasic_fail_unknownProfile() {
        Long id = 1L;
        when(profileRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> profileService.updateImageToBasic(id))
                .isInstanceOf(AuthenticationException.class)
                .extracting("errorCode")
                .isEqualTo(AuthenticationErrorCode.UNKNOWN_USER_PROFILE);
    }
}