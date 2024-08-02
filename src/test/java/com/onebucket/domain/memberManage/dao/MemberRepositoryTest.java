package com.onebucket.domain.memberManage.dao;

import com.onebucket.domain.memberManage.domain.Member;
import org.junit.jupiter.api.*;
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
public class MemberRepositoryTest {

    private final String username = "test-user";
    private final String password = "!1Password1!";
    private final String nickname =  "test-nick";

    private final Member member = Member.builder()
            .username(username)
            .password(password)
            .nickname(nickname)
            .build();


    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(member);
    }


    //-+-+-+-+-+-+]] findByUsername test [[-+-+-+-+-+-+
    @Test
    @DisplayName("findByUsername - success")
    void testFindByUsername_success() {
        //when
        Member findMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Expected member is present, but was not"));

        //then
        assertThat(findMember.getUsername()).isEqualTo(username);
        assertThat(findMember.getNickname()).isEqualTo(nickname);

    }

    @Test
    @DisplayName("findByUsername - fail / unknown user")
    void testFindByUsername_fail_unknownUser() {
        //when & then
        assertThat(memberRepository.findByUsername("invalid name"))
                .isEqualTo(Optional.empty());
    }

    //-+-+-+-+-+-+]] existByUsername test [[-+-+-+-+-+-+
    @Test
    @DisplayName("existsByUsername - success")
    void testExistsByUsername_success() {
        //when
        boolean isExists = memberRepository.existsByUsername(username);
        boolean isNotExists = memberRepository.existsByUsername("invalid name");

        //then
        assertThat(isExists).isTrue();
        assertThat(isNotExists).isFalse();
    }

    //-+-+-+-+-+-+]] deleteByUsername test [[-+-+-+-+-+-+
    @Test
    @DisplayName("deleteByUsername - success")
    void testDeleteByUsername_success() {

        memberRepository.deleteByUsername(username);

        boolean exists = memberRepository.existsByUsername(username);
        assertThat(exists).isFalse();
    }

    //-+-+-+-+-+-+]] save test [[-+-+-+-+-+-+
    @Test
    @DisplayName("save -fail / data duplicate")
    void testSave_fail_duplicate() {
        Member duplicateMember = Member.builder()
                .username(username)
                .nickname(nickname)
                .password(password).build();

        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> memberRepository.save(duplicateMember));
    }

    //-+-+-+-+-+-+]] findIdByUsername test [[-+-+-+-+-+-+
    @Test
    @DisplayName("findIdByUsername - success")
    void testFindIdByUsername_success() {
        memberRepository.findIdByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("expect id of username but not exist user")
        );
    }

    @Test
    @DisplayName("findIdByUsername - fail / unknown user")
    void testFindIdByUsername_fail_unknownUser() {
        Optional<Long> id = memberRepository.findIdByUsername("unknownUser");

        assertThat(id).isEqualTo(Optional.empty());
    }
    //-+-+-+-+-+-+]] findNicknameById test [[-+-+-+-+-+-+
    @Test
    @DisplayName("findNicknameById - success")
    void testFindNicknameById_success() {
        Long id = memberRepository.findIdByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("expect id of username but not exist user")
        );

        String findNick = memberRepository.findNicknameById(id).orElseThrow(
                () -> new IllegalArgumentException("expect nickname but not exist")
        );
        assertThat(findNick).isEqualTo(nickname);
    }

    @Test
    @DisplayName("findNicknameById - fail / unknown user")
    void testFindNicknameById_fail_unknownUser() {
        Optional<String> findNick = memberRepository.findNicknameById(2039847650293L);
        assertThat(findNick).isEqualTo(Optional.empty());
    }
}
