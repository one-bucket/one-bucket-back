package com.onebucket.domain.memberManage.dao;

import com.onebucket.domain.memberManage.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

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
public class
MemberRepositoryTest {
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
    void testFindByUsername_success() {
        //given
        memberRepository.save(member1);
        //when
        Optional<Member> findMember = memberRepository.findByUsername("hongik");
        //then
        assertThat(findMember).isNotNull();
    }

    @Test
    @DisplayName("유저 검색 실패")
    void findByUsernameTestFail() {
        //when & then
        assertThat(memberRepository.findByUsername("invalid name")).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("existsByUsername 테스트 성공")
    void existsByUsernameTestSuccess() {
        //given
        memberRepository.save(member1);

        //when
        boolean isExists = memberRepository.existsByUsername("hongik");
        boolean isNotExists = memberRepository.existsByUsername("invalid name");

        //then
        assertThat(isExists).isTrue();
        assertThat(isNotExists).isFalse();
    }

    @Test
    @DisplayName("deleteByUsername 테스트 성공")
    void deleteByUsernameTestSuccess() {
        memberRepository.save(member1);

        memberRepository.deleteByUsername("hongik");

        boolean exists = memberRepository.existsByUsername("hongik");
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("save 테스트 실패 - 중복된 id")
    void testSave_fail_duplicateId() {
        Member member2 = Member.builder()
                .username("hongik")
                .password("1111")
                .nickname("nicknick")
                .build();

        memberRepository.save(member1);
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> memberRepository.save(member2));
    }

    @Test
    @DisplayName("save 테스트 실패 - 중복된 nickname")
    void testSave_fail_duplicateNickname() {
        Member member2 = Member.builder()
                .username("test")
                .password("1111")
                .nickname("Han")
                .build();

        memberRepository.save(member1);
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> memberRepository.save(member2));
    }
}
