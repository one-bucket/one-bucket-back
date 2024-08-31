package com.onebucket.domain.boardManage.dao;

import com.onebucket.domain.boardManage.dto.internal.board.BoardIdsDto;
import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.boardManage.entity.BoardType;
import com.onebucket.domain.boardManage.entity.Comment;
import com.onebucket.domain.boardManage.entity.post.MarketPost;
import com.onebucket.domain.boardManage.entity.post.Post;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.universityManage.dao.UniversityRepository;
import com.onebucket.domain.universityManage.domain.University;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
@Sql(scripts = "/sql/SetMembers.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class BoardDaoTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardTypeRepository boardTypeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MarketPostRepository marketPostRepository;


    @Autowired
    private CommentRepository commentRepository;


    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final Long defaultId = 100L;
    private final LocalDateTime defaultDateTime = LocalDateTime.of(
            2020,1,1,22,22,22,22222
    );

    @Test
    @DisplayName("save 테스트  - success")
    void testSave_success() {

        //순서 1: board type 정의
        BoardType boardType = BoardType.builder()
                .description("description")
                .name("board type")
                .build();
        BoardType savedBoardType = boardTypeRepository.save(boardType);

        //순서 2: 기존에 정의된 university 와 board type 을 이용해 board 저장
        University university = universityRepository.findById(defaultId).orElseThrow(RuntimeException::new);

        Board board = Board.builder()
                .university(university)
                .boardType(savedBoardType)
                .name("new board")
                .description("description")
                .build();
        Board savedBoard = boardRepository.save(board);

        //순서 3: 해당 board 에 post 저장.
        Member member = memberRepository.findById(defaultId).orElseThrow(RuntimeException::new);

        Post post = Post.builder()
                .board(savedBoard)
                .author(member)
                .title("title")
                .text("text")
                .build();

        Post savedPost = postRepository.save(post);

        MarketPost marketPost = MarketPost.builder()
                .board(savedBoard)
                .author(member)
                .title("market title")
                .text("market text")
                .isFin(false)
                .item("item")
                .location("홍익대학교")
                .wanted(3)
                .build();

        MarketPost savedMarketPost = marketPostRepository.save(marketPost);

        //순서 4: 해당 post 에 대한 comment 저장
        Comment comment = Comment.builder()
                .parentComment(null)  //안 적어도 되나 테스트 코드인 만큼 명시
                .isModified(false)
                .author(member)
                .post(savedPost)
                .text("comment text")
                .build();

        Comment savedComment = commentRepository.save(comment);

        //순서 5: 해당 comment 에 댓글 reply 저장
        Comment reply = Comment.builder()
                .parentComment(savedComment)
                .isModified(false)
                .author(member)
                .post(savedPost)
                .text("reply text")
                .build();
        commentRepository.save(reply);

        Comment mComment = Comment.builder()
                .parentComment(null)  //안 적어도 되나 테스트 코드인 만큼 명시
                .isModified(false)
                .author(member)
                .post(savedMarketPost)
                .text("market comment text")
                .build();

        Comment mSavedComment = commentRepository.save(mComment);

        //순서 5: 해당 comment 에 댓글 reply 저장
        Comment mReply = Comment.builder()
                .parentComment(mSavedComment)
                .isModified(false)
                .author(member)
                .post(savedMarketPost)
                .text("market reply text")
                .build();
        commentRepository.save(mReply);

        assertThat(boardRepository.count()).isEqualTo(1);
        assertThat(boardTypeRepository.count()).isEqualTo(1);
        assertThat(commentRepository.count()).isEqualTo(4);
        assertThat(postRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("save 테스트 - 실패 / board type 생성 전 board 생성 시도")
    void testSave_fail_saveBoardBeforeSaveBoardType() {
        University university = universityRepository.findById(defaultId).orElseThrow(RuntimeException::new);

        Board board = Board.builder()
                .university(university)
                .name("new board")
                .description("description")
                .build();

        assertThatThrownBy(() -> boardRepository.save(board))
                .isInstanceOf(DataIntegrityViolationException.class);

        BoardType boardType = BoardType.builder()
                .id(111L)
                .name("name")
                .build();

        assertThat(boardTypeRepository.findById(111L).isPresent()).isFalse();

        Board board2 = Board.builder()
                .boardType(boardType)
                .university(university)
                .name("new board2")
                .description("description")
                .build();

        assertThatThrownBy(() -> boardRepository.save(board2))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    @DisplayName("findAllBoardUniversityAndBoardTypeIds 테스트 - 성공")
    void testFindAllBoardUniversityAndBoardTypeIds_success() {
        List<BoardIdsDto> ids = boardRepository.findAllBoardUniversityAndBoardTypeIds();

        assertThat(ids.isEmpty()).isTrue();

        BoardType boardType = BoardType.builder()
                .description("description")
                .name("board type")
                .build();
        BoardType savedBoardType = boardTypeRepository.save(boardType);

        University university = universityRepository.findById(defaultId).orElseThrow(RuntimeException::new);

        Board board = Board.builder()
                .id(defaultId)
                .university(university)
                .boardType(savedBoardType)
                .name("new board")
                .description("description")
                .build();
        boardRepository.save(board);


        ids = boardRepository.findAllBoardUniversityAndBoardTypeIds();

        assertThat(ids.size()).isEqualTo(1L);
        BoardIdsDto result = ids.get(0);

        assertThat(result.getUniversityId()).isEqualTo(defaultId);
        assertThat(result.getBoardTypeId()).isEqualTo(savedBoardType.getId());


        // board type 이 여러 개일 때
        BoardType boardType2 = BoardType.builder()
                .description("description")
                .name("board type2")
                .build();
        BoardType savedBoardType2 = boardTypeRepository.save(boardType2);


        Board board2 = Board.builder()
                .id(defaultId + 1)
                .university(university)
                .boardType(savedBoardType2)
                .name("new board2")
                .description("description")
                .build();
        boardRepository.save(board2);

        ids = boardRepository.findAllBoardUniversityAndBoardTypeIds();

        assertThat(ids.size()).isEqualTo(2L);

        University university2 = University.builder()
                .id(defaultId + 1)
                .email("email@mail")
                .address("address")
                .name("university2")
                .build();

        University savedUniv2 = universityRepository.save(university2);

        Board board3 = Board.builder()
                .id(defaultId + 2)
                .university(savedUniv2)
                .boardType(savedBoardType)
                .name("new board3")
                .description("description")
                .build();
        boardRepository.save(board3);

        Board board4 = Board.builder()
                .id(defaultId + 3)
                .university(savedUniv2)
                .boardType(savedBoardType2)
                .name("new board4")
                .description("description")
                .build();
        boardRepository.save(board4);

        ids = boardRepository.findAllBoardUniversityAndBoardTypeIds();
        assertThat(ids.size()).isEqualTo(4L);


        //distinct 여부 검사
        Board board5 = Board.builder()
                .id(defaultId + 4)
                .university(savedUniv2)
                .boardType(savedBoardType2)
                .name("new board5")
                .description("description")
                .build();
        boardRepository.save(board5);
        ids = boardRepository.findAllBoardUniversityAndBoardTypeIds();
        assertThat(ids.size()).isEqualTo(4L);
    }

    @Test
    @DisplayName("post-findByBoardId page 테스트 - 성공")
    void testPostFindByBoardId() {
        University university = universityRepository.findById(100L).orElseThrow(RuntimeException::new);
        Member member = memberRepository.findById(100L).orElseThrow(RuntimeException::new);

        BoardType boardType = BoardType.builder()
                .id(100L)
                .name("name")
                .description("description")
                .build();
        BoardType savedBoardType = boardTypeRepository.save(boardType);

        Board board = Board.builder()
                .id(100L)
                .boardType(savedBoardType)
                .university(university)
                .description("description")
                .name("name")
                .build();
        Board savedBoard = boardRepository.save(board);

        for(long i = 101L; i <= 200L; i++) {
            Post post = Post.builder()
                    .id(i)
                    .board(savedBoard)
                    .author(member)
                    .title("title of " + i)
                    .text("text of " + i)
                    .build();

            postRepository.save(post);
        }

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Post> postsPage = postRepository.findByBoardId(savedBoard.getId(), pageable);
        assertThat(postsPage).isNotNull();
        assertThat(postsPage.getContent().size()).isEqualTo(10);
        assertThat(postsPage.getContent().get(0).getTitle()).isEqualTo("title of 101");

        pageable = PageRequest.of(9, 10, Sort.by("id").ascending());
        postsPage = postRepository.findByBoardId(savedBoard.getId(), pageable);
        assertThat(postsPage).isNotNull();
        assertThat(postsPage.getContent().get(9).getTitle()).isEqualTo("title of 200");

    }


    // 현재 까지의 테스트 코드는 다음과 같은 것을 나타 내기 위해 작성 되었다.
    // 1. 엔티티 간 관계 - 특정 entity 와의 관계 (외래키 사용 등에 대한) 를 명시
    // 2. board 에 대해 커스텀 로직


    @Test
    @DisplayName("boardType-findByName - success")
    void testBoardType_FindByName_success() {
        String query = """
                INSERT INTO board_type (name, description)
                VALUES ('name', 'description');
                """;

        jdbcTemplate.update(query);

        BoardType boardType = boardTypeRepository.findByName("name").orElseThrow(RuntimeException::new);

        assertThat(boardType).isNotNull();
        assertThat(boardType.getDescription()).isEqualTo("description");
    }

}