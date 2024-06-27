package com.onebucket.domain.service;

import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.memberManage.dto.CreateMemberRequestDTO;
import com.onebucket.domain.memberManage.service.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private MemberServiceImpl memberService;

    private Member member() {
        return Member.builder()
                .username("hongik")
                .password("123456")
                .nickname("hahaha")
                .build();
    }

    @Test
    @DisplayName("멤버 생성 성공")
    void createMember() {
        CreateMemberRequestDTO dto = CreateMemberRequestDTO.builder()
                .username("ididid")
                .password("password")
                .nickname("nickname")
                .build();
        memberService.createMember(dto);
        Assertions.assertThat(memberRepository.findAll()).isNotNull();
    }

    @Test
    @DisplayName("멤버 수정 성공")
    void updateMember() {
        Member member = member();
    }
}
