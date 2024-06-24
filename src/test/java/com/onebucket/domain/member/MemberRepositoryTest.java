package com.onebucket.domain.member;

import com.onebucket.domain.member.Member;
import com.onebucket.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * <br>package name   : com. Member
 * <br>file name      : MemberRepositoryTest
 * <br>date           : 2024-06-22
 * <pre>
 * <span style="color: white;">[description]</span>
 * MemberRepository 안에 있는 메소드들을 테스트한다.
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
 * 2024-06-22     SeungHoon              init create
 * </pre>
 */
@DataJpaTest
public class MemberRepositoryTest {
    private Member member1;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        member1 = Member.builder()
                .username("hongik")
                .password("1234")
                .nickname("Han")
                .build();
    }

    @Test
    @DisplayName("findByUsername 테스트 성공")
    void findByUsernameTestSuccess() {
        //given
        memberRepository.save(member1);
        //when
        Optional<Member> findMember = memberRepository.findByUsername("hongik");
        //then
        assertThat(findMember).isNotNull();
    }

    @Test
    @DisplayName("existsByUsername 테스트 성공")
    void existsByUsernameTestSuccess() {
        memberRepository.save(member1);

        boolean exists = memberRepository.existsByUsername("hongik");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("deleteByUsername 테스트 성공")
    void deleteByUsernameTestSuccess() {
        memberRepository.save(member1);

        memberRepository.deleteByUsername("hongik");

        boolean exists = memberRepository.existsByUsername("hongik");
        assertThat(exists).isFalse();
    }
}
