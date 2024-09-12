package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.*;
import com.onebucket.domain.boardManage.dto.internal.comment.CreateCommentDto;
import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.boardManage.entity.Comment;
import com.onebucket.domain.boardManage.entity.post.Post;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.testComponent.testUtils.DataBaseCleaner;
import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <br>package name   : com.onebucket.domain.boardManage.service
 * <br>file name      : ConcurrencyTest
 * <br>date           : 2024-09-12
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 */

@SpringBootTest
@ActiveProfiles("test")
public class ConcurrencyTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardTypeRepository boardTypeRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataBaseCleaner dataBaseCleaner;

    @Test
    @Sql(scripts = "/sql/IntegrationData.sql")
    void testConcurrentAddCommentToPost() throws InterruptedException {
        Member member = memberRepository.findById(1L).orElseThrow();
        Board board = boardRepository.findById(1L).orElseThrow();

        Post post = Post.builder()
                .id(1L)
                .author(member)
                .board(board)
                .text("text of post")
                .title("title of post")
                .build();
        postRepository.save(post);


        CreateCommentDto dto1 = CreateCommentDto.builder()
                .text("text")
                .postId(1L)
                .username("username1")
                .text("text1")
                .build();

        CreateCommentDto dto2 = CreateCommentDto.builder()
                .text("text2")
                .postId(1L)
                .username("username2")
                .text("text2")
                .build();


        int numberOfThreads = 2;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        service.execute(() -> {
            postService.addCommentToPost(dto1);
            latch.countDown();
        });
        service.execute(() -> {
            postService.addCommentToPost(dto2);
            latch.countDown();
        });

        latch.await();

        String sql = "SELECT id, text FROM comment WHERE post_id = ?";
        List<Comment> comments = jdbcTemplate.query(sql, new CommentRowMapper(), 1L);

        assertThat(comments.size()).isEqualTo(2);
        assertThat(comments)
                .extracting(Comment::getText)
                .containsExactlyInAnyOrder("text1", "text2");

        dataBaseCleaner.clean();
    }
    private static class CommentRowMapper implements RowMapper<Comment> {
        @Override
        public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Comment.builder()
                    .id(rs.getLong("id"))
                    .text(rs.getString("text"))
                    .build();
        }
    }

}
