package com.onebucket.global.auth.springSecurity;

import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * <br>package name   : com.onebucket.global.auth.springSecurity
 * <br>file name      : CustomUserDetailsServiceTest
 * <br>date           : 2024-06-27
 * <pre>
 * <span style="color: white;">[description]</span>
 * Test of CustomUserDetailsService. Get Mock of memberRepository to search.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-06-27        jack8              init create
 * </pre>
 */

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks CustomUserDetailsService customUserDetailsService;


    @Test
    @DisplayName("유저 찾기 성공")
    void testLoadUserByUsername_success() {
        //given
        String username = "testuser";
        Member member = Member.builder()
                .username(username)
                .password("password")
                .nickname("nickname")
                .build();

        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.of(member));

        //when
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        //then
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    @DisplayName("유저 찾기 실패")
    void testLoadUserByUsername_fail_invalidUsername() {
        //given
        String username = "invalidUser";

        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        //when & then
        assertThrows(UsernameNotFoundException.class, () ->
                customUserDetailsService.loadUserByUsername(username));
    }
}