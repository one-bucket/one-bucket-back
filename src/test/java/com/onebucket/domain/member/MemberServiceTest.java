package com.onebucket.domain.member;

import com.onebucket.domain.member.Member;
import com.onebucket.domain.member.repository.MemberRepository;
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
    };

    @Test
    @DisplayName("멤버생성성공")
    void createMember() {

    }
}
