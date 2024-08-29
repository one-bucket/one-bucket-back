package com.onebucket.domain.boardManage.dao;

import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dao
 * <br>file name      : BoardRepositoryTest
 * <br>date           : 2024-08-28
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-08-28        jack8              init create
 * </pre>
 */

@DataJpaTest
@Sql(scripts = "/sql/BoardRepositoryTest.sql")
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Sql(scripts = "/sql/BoardRepositoryTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void testSql() {

        Member member = memberRepository.findById(100L).get();
        System.out.println(member.getNickname());
    }


}