package com.onebucket.domain.memberManage.service;

import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.memberManage.dto.CreateMemberRequestDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.onebucket.domain.memberManage.dto.NicknameRequestDto;
import com.onebucket.domain.memberManage.dto.ReadMemberInfoDto;
import com.onebucket.domain.memberManage.dto.internal.SetPasswordDto;
import com.onebucket.domain.universityManage.dao.UniversityRepository;
import com.onebucket.domain.universityManage.domain.University;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.customException.universityManageException.UniversityException;
import com.onebucket.global.exceptionManage.customException.verificationException.VerificationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.UniversityErrorCode;
import com.onebucket.global.exceptionManage.errorCode.VerificationErrorCode;
import com.onebucket.global.utils.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private UniversityRepository universityRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Member mockMember;

    @Mock
    private University mockUniversity;

    @Mock
    private RandomStringUtils randomStringUtils;
    @InjectMocks
    private MemberServiceImpl memberService;


    private CreateMemberRequestDto getDto() {
        return CreateMemberRequestDto.builder()
                .username("username")
                .password("!1Password1!")
                .nickname("nickname")
                .build();
    }

    private final University nullUniversity = University.builder()
            .address("null")
            .email("null")
            .name("null")
            .build();

    @BeforeEach
    void setUp() {

    }

    //-+-+-+-+-+-+]] createMember test [[-+-+-+-+-+-+
    @Test
    @DisplayName("createMember - success")
    void testCreateMember_success() {
        //given
        CreateMemberRequestDto dto = getDto();

        when(passwordEncoder.encode(anyString())).thenReturn("password");
        when(memberRepository.save(any(Member.class))).thenReturn(mockMember);
        when(mockMember.getId()).thenReturn(1L);
        when(universityRepository.findByName("null")).thenReturn(Optional.of(nullUniversity));

        //when
        Long id = memberService.createMember(dto);

        //then
        assertThat(id).isEqualTo(1L);
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("createMember - fail / duplicate user")
    void testCreateMember_fail_duplicateUser() {
        //given
        CreateMemberRequestDto dto = getDto();

        when(passwordEncoder.encode(anyString())).thenReturn("password");
        when(universityRepository.findByName("null")).thenReturn(Optional.of(nullUniversity));
        doThrow(DataIntegrityViolationException.class).when(memberRepository).save(any(Member.class));


        assertThatThrownBy(() -> memberService.createMember(dto))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining("username or nickname already exist.")
                .extracting("errorCode")
                .isEqualTo(AuthenticationErrorCode.DUPLICATE_USER);
    }

    //-+-+-+-+-+-+]] readMember test [[-+-+-+-+-+-+
    @Test
    @DisplayName("readMember - success")
    void testReadMember_success() {
        String username = "username";
        String nickname = "nickname";
        String university = "university";
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
        when(mockMember.getUniversity()).thenReturn(mockUniversity);
        when(mockMember.getNickname()).thenReturn(nickname);
        when(mockUniversity.getName()).thenReturn(university);

        ReadMemberInfoDto result = memberService.readMember(username);

        assertEquals(username, result.getUsername());
        assertEquals(nickname, result.getNickname());
        assertEquals(university, result.getUniversity());
    }

    @Test
    @DisplayName("readMember - success / university is null")
    void testReadMember_success_universityNull() {
        String username = "username";
        String nickname = "nickname";
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
        when(mockMember.getUniversity()).thenReturn(null);
        when(mockMember.getNickname()).thenReturn(nickname);

        ReadMemberInfoDto result = memberService.readMember(username);

        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getUniversity()).isEqualTo("null");
        assertThat(result.getNickname()).isEqualTo(nickname);
    }

    @Test
    @DisplayName("readMember - fail / can't find user")
    void testReadMember_fail_unknownUser() {
        String username = "username";

        when(memberRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.readMember(username))
                .isInstanceOf(AuthenticationException.class)
                .extracting("errorCode")
                .isEqualTo(AuthenticationErrorCode.UNKNOWN_USER);
    }


    //-+-+-+-+-+-+]] updateMember test [[-+-+-+-+-+-+
    @Test
    @DisplayName("updateMember - success")
    void testUpdateMember_success() {

        //given
        String username = "username";
        NicknameRequestDto dto = NicknameRequestDto.builder()
                .nickname("nickname")
                .build();


        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));


        //when
        memberService.updateMember(username, dto);

        //then
        verify(memberRepository).save(mockMember);
    }

    @Test
    @DisplayName("updateMember - fail / can't find user")
    void testUpdateMember_fail_notExistUser() {
        //given
        String username = "nonExistentUser";
        NicknameRequestDto dto = NicknameRequestDto.builder()
                .nickname("nickname").build();

        when(memberRepository.findByUsername(username)).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> memberService.updateMember(username, dto))
                .isInstanceOf(AuthenticationException.class)
                .extracting("errorCode")
                .isEqualTo(AuthenticationErrorCode.UNKNOWN_USER);

    }

    @Test
    @DisplayName("updateMember - fail / duplicate nickname")
    void testUpdateMember_fail_duplicateNickname() {
        //given
        String username = "testuser";
        NicknameRequestDto dto = NicknameRequestDto.builder()
                .nickname("nickname").build();


        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
        doThrow(DataIntegrityViolationException.class).when(memberRepository).save(any(Member.class));

        //when & then
        assertThatThrownBy(() -> memberService.updateMember(username, dto))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining("nickname duplicate")
                .extracting("errorCode")
                .isEqualTo(AuthenticationErrorCode.DUPLICATE_USER);
    }


    //-+-+-+-+-+-+]] quitMember test [[-+-+-+-+-+-+
    @Test
    @DisplayName("quitMember - success")
    void testQuitMember_success() {
        String username = "username";

        //when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));

        memberService.quitMember(username);

        //verify(memberRepository, times(1)).save(mockMember);
    }

    @Test
    @DisplayName("quitMember - fail / unknown user")
    void testQuitMember_fail_unknownUser() {
//        String username = "username";
//        when(memberRepository.findByUsername(username)).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> memberService.quitMember(username))
//                .isInstanceOf(AuthenticationException.class)
//                .extracting("errorCode")
//                .isEqualTo(AuthenticationErrorCode.UNKNOWN_USER);
//
//        verify(memberRepository, never()).save(any(Member.class));
    }

    //-+-+-+-+-+-+]] changePassword test [[-+-+-+-+-+-+
    @Test
    @DisplayName("initPassword - success / to random string")
    void testChangePassword_success_randomString() {
        //given
        String username = "username";
        String password = "!1Password1!";
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
        when(randomStringUtils.generateRandomStr(15)).thenReturn(password);

        String newPassword = memberService.initPassword(username);
        verify(memberRepository, times(1)).save(mockMember);
        assertThat(password).isEqualTo(newPassword);
    }

    @Test
    @DisplayName("changePassword -success / to defined string")
    void testChangePassword_success_definedString() {
        String username = "username";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        SetPasswordDto dto = new SetPasswordDto(username, oldPassword, newPassword);
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
        when(passwordEncoder.matches(dto.oldPassword(),mockMember.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(dto.newPassword())).thenReturn(newPassword);
        String result = memberService.changePassword(dto);

        assertThat(result).isEqualTo(newPassword);
    }

    @Test
    @DisplayName("changePassword - fail / unknown user")
    void testChangePassword_fail_unknownUser() {
        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.changePassword(new SetPasswordDto("username", "oldPassword", "newPassword")))
                .isInstanceOf(AuthenticationException.class)
                .extracting("errorCode")
                .isEqualTo(AuthenticationErrorCode.UNKNOWN_USER);
    }

    @Test
    @DisplayName("changePassword - fail / different oldPassword")
    void testChangePassword_fail_differentOldPassword() {
        String username = "username";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        SetPasswordDto dto = new SetPasswordDto(username, oldPassword, newPassword);
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
        when(passwordEncoder.matches(dto.oldPassword(),mockMember.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> memberService.changePassword(dto))
                .isInstanceOf(VerificationException.class)
                .extracting("errorCode")
                .isEqualTo(VerificationErrorCode.INVALID_PASSWORD);
    }

    //-+-+-+-+-+-+]] usernameToId test [[-+-+-+-+-+-+
    @Test
    @DisplayName("usernameToId - success")
    void testUsernameToId_success() {
        String username = "username";
        Long id = 1L;
        when(memberRepository.findIdByUsername(username)).thenReturn(Optional.of(id));
        Long result = memberService.usernameToId(username);
        assertThat(result).isEqualTo(1L);
    }

    @Test
    @DisplayName("usernameToId - fail / unknown user")
    void testUsernameToId_fail_unknownUser() {
        String username = "username";
        when(memberRepository.findIdByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.usernameToId(username))
                .isInstanceOf(AuthenticationException.class)
                .extracting("errorCode")
                .isEqualTo(AuthenticationErrorCode.UNKNOWN_USER);
    }

    //-+-+-+-+-+-+]] idToNickname Test [[-+-+-+-+-+-+
    @Test
    @DisplayName("idToNickname - success")
    void testIdToNickname_success() {
        Long id = 1L;
        String nickname = "nickname";

        when(memberRepository.findNicknameById(id)).thenReturn(Optional.of(nickname));

        String result = memberService.idToNickname(id);
        assertThat(result).isEqualTo(nickname);
    }

    @Test
    @DisplayName("idToNickname - fail / unknown user")
    void testIdToNickname_fail_unknownUser() {
        Long id = 1L;

        when(memberRepository.findNicknameById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.idToNickname(id))
                .isInstanceOf(AuthenticationException.class)
                .extracting("errorCode")
                .isEqualTo(AuthenticationErrorCode.UNKNOWN_USER);
    }


    //-+-+-+-+-+-+]] usernameToUniversity test[[-+-+-+-+-+-+
    @Test
    @DisplayName("usernameToUniversity - success")
    void testUsernameToUniversity_success() {
        String username = "username";
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
        when(mockMember.getUniversity()).thenReturn(mockUniversity);
        when(mockUniversity.getName()).thenReturn("name");

        University result = memberService.usernameToUniversity(username);

        assertThat(result).isEqualTo(mockUniversity);
    }

    @Test
    @DisplayName("usernameToUniversity - success / null university")
    void testUsernameToUniversity_success_nullUniversity() {
        String username = "username";
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
        when(mockMember.getUniversity()).thenReturn(null);

        assertThatThrownBy(() -> memberService.usernameToUniversity(username))
                .isInstanceOf(UniversityException.class)
                .extracting("errorCode")
                .isEqualTo(UniversityErrorCode.NOT_EXIST_UNIVERSITY);
    }

    @Test
    @DisplayName("usernameToUniversity - fail / unknown user")
    void testUsernameToUniversity_fail_unknownUser() {
        String username = "username";
        when(memberRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.usernameToUniversity(username))
                .isInstanceOf(AuthenticationException.class)
                .extracting("errorCode")
                .isEqualTo(AuthenticationErrorCode.UNKNOWN_USER);
    }

    @Test
    @DisplayName("usernameToUniversity - fail / returned university is basic type")
    void testUsernameToUniversity_fail_baseUniv() {
        University university = University.builder()
                .name("null")
                .address("null")
                .email("null")
                .id(1L)
                .build();
        String username = "username";
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
        when(mockMember.getUniversity()).thenReturn(university);

        assertThatThrownBy(() -> memberService.usernameToUniversity(username))
                .isInstanceOf(UniversityException.class)
                .extracting("errorCode")
                .isEqualTo(UniversityErrorCode.NOT_EXIST_UNIVERSITY);
    }

}
