package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.*;
import com.onebucket.domain.boardManage.dto.internal.comment.CreateCommentDto;
import com.onebucket.domain.boardManage.dto.internal.post.PostAuthorDto;
import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.boardManage.entity.Comment;
import com.onebucket.domain.boardManage.entity.post.Post;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.global.redis.RedisRepository;
import org.junit.jupiter.api.DisplayName;
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

import static java.lang.Thread.sleep;
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
    private MemberRepository memberRepository;
    @Autowired
    private LikesMapRepository likesMapRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private SyncDataScheduledService syncDataScheduledService;

    @Test
    @Sql(scripts = "/sql/IntegrationData.sql")
    void testConcurrentAddCommentToPost() throws InterruptedException {
        Member member = memberRepository.findById(1L).orElseThrow();
        Board board = boardRepository.findById(1L).orElseThrow();

        Post post = Post.builder()
                .author(member)
                .board(board)
                .text("text of post")
                .title("title of post")
                .build();
        Long postId = postRepository.save(post).getId();


        CreateCommentDto dto1 = CreateCommentDto.builder()
                .text("text")
                .postId(postId)
                .username("username1")
                .text("text1")
                .build();

        CreateCommentDto dto2 = CreateCommentDto.builder()
                .text("text2")
                .postId(postId)
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
        List<Comment> comments = jdbcTemplate.query(sql, new CommentRowMapper(), postId);

        assertThat(comments.size()).isEqualTo(2);
        assertThat(comments)
                .extracting(Comment::getText)
                .containsExactlyInAnyOrder("text1", "text2");

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

    @Test
    @Sql(scripts = "/sql/IntegrationData.sql")
    @DisplayName("test concurrent increase view count")
    void testConcurrent_increaseViewCount() throws InterruptedException {
        Member member = memberRepository.findById(1L).orElseThrow();
        Board board = boardRepository.findById(1L).orElseThrow();

        Post post = Post.builder()
                .author(member)
                .board(board)
                .text("text of post")
                .title("title of post")
                .build();
        Post savedPost = postRepository.save(post);
        Long postId = savedPost.getId();

        PostAuthorDto dto1 = PostAuthorDto.builder()
                .username("username1")
                .postId(postId)
                .build();

        PostAuthorDto dto2 = PostAuthorDto.builder()
                .username("username2")
                .postId(postId)
                .build();

        PostAuthorDto dto3 = PostAuthorDto.builder()
                .username("username3")
                .postId(postId)
                .build();

        int numberOfThreads = 5;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        service.execute(() -> {
            postService.increaseViewCount(dto1);
            latch.countDown();
        });

        service.execute(() -> {
            try {
                sleep(150);
                postService.increaseViewCount(dto1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            latch.countDown();
        });

        service.execute(() -> {
            try {
                sleep(50);
                postService.increaseViewCount(dto1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            latch.countDown();
        });

        service.execute(() -> {
            try {
                sleep(50);
                postService.increaseViewCount(dto2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            latch.countDown();
        });
        service.execute(() -> {
            try {
                sleep(50);
                postService.increaseViewCount(dto3);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            latch.countDown();
        });

        latch.await();

        String query = """
                SELECT views FROM post WHERE id = ?
                """;
        Long viewCount = jdbcTemplate.queryForObject(query, Long.class, postId);

        assertThat(viewCount).isEqualTo(3L);

        redisRepository.flushAll();
    }

    /**
     * <pre>
     * redis에서 조회수 관련 항목을 조회하여 db에 이를 업데이트 하는 로직에 대한 동시성을 테스트한다.
     * 코드에서는 lua 스크립트를 사용하여 데이터를 검색하고 이를 반환하며 삭제하는 로직을
     * 하나의 트랜잭션으로 처리하여 중간에 난입되는 갱신에 대하여 해당 스크립트가
     * 끝난 이후에 실행 되도록 하였다.
     * 실제 해당 코드에서 sleep을 0으로 가깝게 두면, 해당 스크립트가 실행 전에 끼어들어,
     * 반영이 되고, 조금 크게 둔다면 스크립트가 온전히 실행되고 이후 redis에
     * 저장되므로 redis에 값이 남아 있다.
     * </pre>
     *
     * @throws InterruptedException 멀티 스레딩 테스트에 대한 예외
     */
    @Test
    @Sql(scripts = "/sql/IntegrationData.sql")
    @DisplayName("test concurrent syncLikes from redis to post")
    void testConcurrentSynLikesFromRedisToPost() throws InterruptedException {
        redisRepository.flushAll();
        Member member = memberRepository.findById(1L).orElseThrow();
        Board board = boardRepository.findById(1L).orElseThrow();

        Post post = Post.builder()
                .author(member)
                .board(board)
                .text("text of post")
                .title("title of post")
                .build();
        Long postId = postRepository.save(post).getId();

        for(int i = 1; i <= 5; i++) {
            postService.increaseLikesCount(postId, (long) i);
        }

        Long likesToAdd = Long.parseLong(redisRepository.get("post:likes:1"));
        assertThat(likesToAdd).isEqualTo(5);
        Long likesInPost = postRepository.findById(postId).orElseThrow().getLikes();
        assertThat(likesInPost).isEqualTo(0);


        int numberOfThreads = 2;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        service.execute(() -> {
            syncDataScheduledService.syncLikesFromRedisToPost();
            latch.countDown();
        });
        service.execute(() -> {
            try {
                sleep(50);
                redisRepository.increaseValue("post:likes:" + postId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            latch.countDown();
        });

        latch.await();

        likesInPost = postRepository.findById(postId).orElseThrow().getLikes();
        assertThat(likesInPost).isEqualTo(5);

        assertThat(redisRepository.get("post:likes:" + postId)).isEqualTo("1");
        Long count = likesMapRepository.countByPostId(postId);
        assertThat(count).isEqualTo(5);

        redisRepository.flushAll();

    }
}
