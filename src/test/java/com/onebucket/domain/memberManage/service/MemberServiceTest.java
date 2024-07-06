package com.onebucket.domain.memberManage.service;

import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.memberManage.dto.CreateMemberRequestDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.onebucket.domain.memberManage.dto.UpdateNicknameRequestDto;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
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
    private PasswordEncoder passwordEncoder;

    @Mock
    private Member mockMember;

    @Mock
    private RandomStringUtils randomStringUtils;
    @InjectMocks
    private MemberServiceImpl memberService;



    private CreateMemberRequestDto getDto() {
        return  CreateMemberRequestDto.builder()
                .username("testuser")
                .password("password")
                .nickname("nickname")
                .build();
    }

    @BeforeEach
    void setUp() {
        lenient().when(passwordEncoder.encode(anyString())).thenReturn("password");
    }
    @Test
    @DisplayName("멤버 생성 성공")
    void testCreateMember_success() {
        //given
        CreateMemberRequestDto dto = getDto();

        when(memberRepository.save(any(Member.class))).thenReturn(mockMember);
        when(mockMember.getId()).thenReturn(1L);

        //when
        Long id = memberService.createMember(dto);
        assertEquals(id, 1L);

        //then
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("멤버 생성 실패 - 중복된 유저")
    void testCreateMember_fail() {
        //given
        CreateMemberRequestDto dto = getDto();

        doThrow(DataIntegrityViolationException.class).when(memberRepository).save(any(Member.class));

        assertThrows(AuthenticationException.class ,() ->
            memberService.createMember(dto)
        );
    }

    @Test
    @DisplayName("멤버 업데이트 성공")
    void testUpdateMember_success() {
        //given
        String username = "testuser";
        UpdateNicknameRequestDto dto = UpdateNicknameRequestDto.builder()
                .nickname("nickname")
                .build();
        Member member = Member.builder()
                .username(username)
                .password("password")
                .nickname("oldname")
                .build();

        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(member));

        //when
        memberService.updateMember(username, dto);

        //then
        verify(memberRepository).save(member);
    }

    @Test
    @DisplayName("멤버 업데이트 실패 - 존재하지 않는 유저")
    void testUpdateMember_fail_notExistUser() {
        //given
        String username = "nonExistentUser";
        UpdateNicknameRequestDto dto = UpdateNicknameRequestDto.builder()
                .nickname("nickname").build();

        when(memberRepository.findByUsername(username)).thenReturn(Optional.empty());

        //when & then
        assertThrows(AuthenticationException.class, () ->
            memberService.updateMember(username, dto)
        );
    }

    @Test
    @DisplayName("멤버 업데이트 실패 - 중복된 닉네임 입력")
    void testUpdateMember_fail_duplicateNickname() {
        //given
        String username = "testuser";
        UpdateNicknameRequestDto dto = UpdateNicknameRequestDto.builder()
                .nickname("nickname").build();
        Member member = Member.builder()
                .username(username)
                .password("password")
                .nickname("oldname")
                .build();

        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(member));
        doThrow(DataIntegrityViolationException.class).when(memberRepository).save(any(Member.class));

        //when & then
        assertThrows(DataIntegrityViolationException.class, () ->
            memberService.updateMember(username, dto)
        );
    }

    @Test
    @DisplayName("비밀번호 변경 성공 - 무작위 문자열로")
    void testChangePassword_success_randomString() {
        //given
        String username = "username";
        Member member = Member.builder()
                .username(username)
                .password("oldPassword")
                .nickname("nickname")
                .build();

        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(member));
        when(randomStringUtils.generateRandomStr(15)).thenReturn("newPassword");

        String newPassword = memberService.changePassword(username);
        verify(memberRepository,times(1)).save(member);
        assertEquals(newPassword, "newPassword");
    }

    @Test
    @DisplayName("비밀번호 변경 성공 - 설정한 문자열로")
    void testChangePassword_success_definedString() {
        String username = "username";
        String newPassword = "newPassword";
        Member member = Member.builder()
                .username(username)
                .password("oldPassword")
                .nickname("nickname")
                .build();

        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(member));

        String result = memberService.changePassword(username, newPassword);
        assertEquals(result, newPassword);
    }

    @Test
    @DisplayName("비밀번호 변경 실패 - unknown user")
    void testChangePassword_fail_unknownUser() {

        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> memberService.changePassword("username"));
    }

}
