package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.BoardRepository;
import com.onebucket.domain.boardManage.dao.CommentRepository;
import com.onebucket.domain.boardManage.dao.LikesMapRepository;
import com.onebucket.domain.boardManage.dao.PostRepository;
import com.onebucket.domain.boardManage.dto.internal.comment.CreateCommentDto;
import com.onebucket.domain.boardManage.dto.parents.PostDto;
import com.onebucket.domain.boardManage.dto.parents.ValueDto;
import com.onebucket.domain.boardManage.entity.*;
import com.onebucket.domain.boardManage.entity.post.Post;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.universityManage.domain.University;
import com.onebucket.global.exceptionManage.customException.boardManageException.BoardManageException;
import com.onebucket.global.exceptionManage.customException.boardManageException.UserBoardException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.BoardErrorCode;
import com.onebucket.global.minio.MinioRepository;
import com.onebucket.global.redis.RedisRepository;
import com.onebucket.global.utils.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * <br>package name   : com.onebucket.domain.boardManage.service
 * <br>file name      : PostServiceTest
 * <br>date           : 9/10/24
 * <pre>
 * <span style="color: white;">[description]</span>
 * test of {@link PostService}.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 */


@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private LikesMapRepository likesMapRepository;
    @Mock
    private SecurityUtils securityUtils;
    @Mock
    private RedisRepository redisRepository;
    @Mock
    private Member mockMember;
    @Mock
    private University mockUniversity;
    @Mock
    private Post mockPost;
    @Mock
    private Comment mockComment;
    @Mock
    private Board mockBoard;
    @Mock
    private BoardType mockBoardType;
    @Mock
    private MinioRepository minioRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(postService, "MAX_SIZE", 300);
        ReflectionTestUtils.setField(postService, "EXPIRE_HOUR", 4L);
    }




    //-+-+-+-+-+-+]] createPost [[-+-+-+-+-+-+
    @Test
    @DisplayName("createPost - success")
    void testCreatePost_success() {

        Long userId = 1L;
        Long univId = 1L;
        Long boardId = 1L;
        PostDto.Create createDto = PostDto.Create.builder()
                .boardId(boardId)
                .univId(univId)
                .userId(userId)
                .title("title")
                .text("text")
                .build();
        when(memberRepository.findById(userId)).thenReturn(Optional.of(mockMember));
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(mockBoard));

        when(postRepository.save(any(Post.class))).thenReturn(mockPost);
        when(mockPost.getId()).thenReturn(100L);

        Long postId = postService.createPost(createDto);

        verify(postRepository, times(1)).save(any(Post.class));
        assertThat(postId).isEqualTo(100L);
    }


    @Test
    @DisplayName("createPost - fail / unknown board")
    void testCreateBoard_fail_unknownBoard() {
        Long userId = 1L;
        Long univId = 1L;
        Long boardId = 1L;
        PostDto.Create createDto = PostDto.Create.builder()
                .boardId(boardId)
                .univId(univId)
                .userId(userId)
                .title("title")
                .text("text")
                .build();
        when(memberRepository.findById(userId)).thenReturn(Optional.of(mockMember));
        when(boardRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.createPost(createDto))
                .isInstanceOf(BoardManageException.class)
                .extracting("errorCode")
                .isEqualTo(BoardErrorCode.UNKNOWN_BOARD);

        verify(postRepository, never()).save(any(Post.class));
    }

    //-+-+-+-+-+-+]] deletePost [[-+-+-+-+-+-+
    @Test
    @DisplayName("deletePost - success")
    void testDeletePost_success() {
        Long userId = 100L;
        Long postId = 1L;
        ValueDto.FindPost dto = ValueDto.FindPost.builder()
                .postId(postId)
                .userId(userId)
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));
        when(mockPost.getAuthor()).thenReturn(mockMember);
        when(mockMember.getId()).thenReturn(userId);

        postService.deletePost(dto);
        verify(postRepository, times(1)).delete(any(Post.class));
    }

    @Test
    @DisplayName("deletePost - fail / unknown post")
    void testDeletePost_fail_unknownPost() {
        Long userId = 100L;
        Long postId = 1L;
        ValueDto.FindPost dto = ValueDto.FindPost.builder()
                .postId(postId)
                .userId(userId)
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.deletePost(dto))
                .isInstanceOf(BoardManageException.class)
                .extracting("errorCode")
                .isEqualTo(BoardErrorCode.UNKNOWN_POST);

        verify(postRepository, never()).delete(any(Post.class));
    }

    @Test
    @DisplayName("deletePost - fail / not author's post")
    void testDeletePost_fail_unmatchedUser() {

        Long userId = 100L;
        Long postId = 1L;
        ValueDto.FindPost dto = ValueDto.FindPost.builder()
                .postId(postId)
                .userId(userId)
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));
        when(mockPost.getAuthor()).thenReturn(mockMember);
        when(mockMember.getId()).thenReturn(101L);

        assertThatThrownBy(() -> postService.deletePost(dto))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining("you are not allowed to edit this post")
                .extracting("errorCode")
                .isEqualTo(AuthenticationErrorCode.UNAUTHORIZED_ACCESS);

        verify(postRepository, never()).delete(any(Post.class));
    }

    @Test
    @DisplayName("deletePost - fail / no author in post") //becuase author may exit this service.
    void testDeletePost_fail_noAuthorInPost() {
        Long userId = 100L;
        Long postId = 1L;
        ValueDto.FindPost dto = ValueDto.FindPost.builder()
                .postId(postId)
                .userId(userId)
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));
        when(mockPost.getAuthor()).thenReturn(null);

        assertThatThrownBy(() -> postService.deletePost(dto))
                .isInstanceOf(BoardManageException.class)
                .extracting("errorCode")
                .isEqualTo(BoardErrorCode.I_AM_AN_APPLE_PIE);
    }

    //-+-+-+-+-+-+]] addCommentToPost [[-+-+-+-+-+-+
    @Test
    @DisplayName("addCommentToPost - success / parent comment(1st layer)")
    void testAddCommentToPost_success_1stLayer() {
        String username = "username";
        Long postId = 1L;
        CreateCommentDto dto = CreateCommentDto.builder()
                .username(username)
                .text("text")
                .postId(postId)
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));

        postService.addCommentToPost(dto);
        verify(mockPost, times(1)).addComment(any(Comment.class));
        verify(postRepository, times(1)).save(mockPost);
    }

    @Test
    @DisplayName("addCommentToPost - success / child comment(2nd layer)")
    void testAddCommentToPost_success_2ndLayer() {
        String username = "username";
        Long postId = 1L;
        Long parentCommentId = 100L;
        CreateCommentDto dto = CreateCommentDto.builder()
                .username(username)
                .text("text")
                .postId(postId)
                .parentCommentId(parentCommentId)
                .build();

        List<Comment> replies = new ArrayList<>();
        for(long i = 100L; i <= 110L; i++) {
            Comment comment = Comment.builder()
                    .id(i)
                    .layer(0)
                    .build();

            replies.add(comment);
        }

        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
        when(mockPost.getComments()).thenReturn(replies);

        postService.addCommentToPost(dto);

        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("addCommentToPost - fail / 3rd layer")
    void testAddCommentToPost_fail_3rdLayer() {
        String username = "username";
        Long postId = 1L;
        Long parentCommentId = 100L;
        CreateCommentDto dto = CreateCommentDto.builder()
                .username(username)
                .text("text")
                .postId(postId)
                .parentCommentId(parentCommentId)
                .build();

        List<Comment> replies = new ArrayList<>();
        for(long i = 100L; i <= 110L; i++) {
            Comment comment = Comment.builder()
                    .id(i)
                    .layer(1)
                    .build();

            replies.add(comment);
        }

        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
        when(mockPost.getComments()).thenReturn(replies);

        assertThatThrownBy(() -> postService.addCommentToPost(dto))
                .isInstanceOf(BoardManageException.class)
                        .extracting("errorCode")
                .isEqualTo(BoardErrorCode.COMMENT_LAYER_OVERHEAD);

        verify(commentRepository, never()).save(any(Comment.class));
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    @DisplayName("addCommentToPost - fail / unknown parent comment")
    void testAddCommentToPost_fail_unknownParentComment() {
        String username = "username";
        Long postId = 1L;
        Long parentCommentId = 100L;
        CreateCommentDto dto = CreateCommentDto.builder()
                .username(username)
                .text("text")
                .postId(postId)
                .parentCommentId(parentCommentId)
                .build();

        List<Comment> replies = new ArrayList<>();
        for(long i = 101L; i <= 110L; i++) {
            Comment comment = Comment.builder()
                    .id(i)
                    .layer(1)
                    .build();

            replies.add(comment);
        }

        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
        when(mockPost.getComments()).thenReturn(replies);

        assertThatThrownBy(() -> postService.addCommentToPost(dto))
                .isInstanceOf(UserBoardException.class)
                .extracting("errorCode")
                .isEqualTo(BoardErrorCode.UNKNOWN_COMMENT);

        verify(commentRepository, never()).save(any(Comment.class));
        verify(postRepository, never()).save(any(Post.class));
    }
    //-+-+-+-+-+-+]] deleteCommentFromPost [[-+-+-+-+-+-+
    /**
     * <pre>
     * 1. find post from postId
     * 2. get list of comments in post.
     * 3. get one comemnt that given in parameter, used stream filter
     * 4. delete comment by method in {@link Post} entity.
     * 5. save entity.
     * </pre>
     * @see PostRepository
     */
    @Test
    @DisplayName("deleteCommentFromPost - success")
    void testDeleteCommentFromPost_success() {
        Long postId = 1L;
        Long commentId = 10L;
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));
        when(mockComment.getPostId()).thenReturn(postId);


        ValueDto.FindComment dto = ValueDto.FindComment.builder()
                .postId(postId)
                .commentId(commentId)
                .build();

        postService.deleteCommentFromPost(dto);
        verify(commentRepository, times(1)).delete(mockComment);
    }

    @Test
    @DisplayName("deleteCommentFromPost - fail / No post to delete")
    void testDeleteCommentFromPost_fail_noPostToDelete() {
        Long postId = 1L;
        Long commentId = 10L;
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));
        when(mockComment.getPostId()).thenReturn(11L);


        ValueDto.FindComment dto = ValueDto.FindComment.builder()
                .postId(postId)
                .commentId(commentId)
                .build();

        assertThatThrownBy(() -> postService.deleteCommentFromPost(dto))
                .isInstanceOf(UserBoardException.class)
                .extracting(("errorCode"))
                .isEqualTo(BoardErrorCode.UNKNOWN_POST);
        verify(commentRepository, never()).delete(any(Comment.class));
    }



    //-+-+-+-+-+-+]] getPostsByBoard [[-+-+-+-+-+-+

    /**
     * 1. Get Page and board id from param
     * 2. find board from database, and convert to {@link PostDto.Thumbnail}
     *
     * Return of findByBoardId maybe empty. (But not null)
     */

    @Test
    @DisplayName("getPostsByBoard - success")
    void testGetPostsByBoard_success() {
        List<Post> listedPost = new ArrayList<>();
        for(long i = 1L; i <= 5L; i++) {
            listedPost.add(Post.builder()
                    .id(i)
                    .build());
        }

        Long boardId = 1L;

        Pageable pageable = Pageable.ofSize(5);
        Page<Post> posts = new PageImpl<>(listedPost, PageRequest.of(0, listedPost.size()), listedPost.size());

        ValueDto.PageablePost dto = ValueDto.PageablePost
                .builder()
                .boardId(boardId)
                .pageable(pageable)
                .build();

        when(postRepository.findByBoardId(boardId, pageable)).thenReturn(posts);

        Page<PostDto.Thumbnail> thumbnails = postService.getPostsByBoard(dto);
        List<PostDto.Thumbnail> listedThumbnails = thumbnails.getContent();
        assertThat(listedThumbnails.size()).isEqualTo(5);
        assertThat(listedThumbnails)
                .extracting("postId")
                .containsAll(List.of(1L, 2L,3L, 4L, 5L));
    }

    @Test
    @DisplayName("getPostsByBoard - success / no post returned")
    void testGetPostsByBoard_success_noPostReturned() {
        Long boardId = 1L;
        Pageable pageable = Pageable.ofSize(5);
        when(postRepository.findByBoardId(boardId, pageable)).thenReturn(Page.empty());
        ValueDto.PageablePost dto = ValueDto.PageablePost
                .builder()
                .boardId(boardId)
                .pageable(pageable)
                .build();

        Page<PostDto.Thumbnail> emptyPostPage = postService.getPostsByBoard(dto);
        List<PostDto.Thumbnail> listedThumbnails = emptyPostPage.getContent();
        assertThat(listedThumbnails.size()).isEqualTo(0);
    }

    //-+-+-+-+-+-+]] getPost [[-+-+-+-+-+-+
    @Test
    @DisplayName("getPost - success")
    void testGetPost_success() {
        Long postId = 1L;
        List<Comment> comments = new ArrayList<>();
        for(long i = 1L; i <= 5L; i++) {
            comments.add(Comment.builder()
                    .id(i)
                    .post(mockPost)
                    .author(mockMember)
                    .text("text" + i)
                    .modifiedDate(LocalDateTime.now())
                    .build());
        }

        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));
        when(mockPost.getComments()).thenReturn(comments);
        when(mockPost.getId()).thenReturn(postId);
        when(mockMember.getNickname()).thenReturn("nick");

        ValueDto.GetPost dto = ValueDto.GetPost.builder()
                .postId(postId)
                .build();

        PostDto.Info result = postService.getPost(dto);
        assertThat(result).isNotNull();
        assertThat(result.getPostId()).isEqualTo(postId);
        assertThat(result.getComments())
                .extracting("commentId")
                .containsAll(List.of(1L, 2L, 3L, 4L, 5L));

    }


    //-+-+-+-+-+-+]] increaseViewCount [[-+-+-+-+-+-+
    /**
     * <pre>
     * 1. get userId, postId from param.
     * 2. set prefix used in redis, and check if value is exist in redis already.
     * 3. if no key exist(for example, null value that return from redis),
     *      plus one views column in database, and add new data in
     *      redis.
     * 4. To manage overhead cause of too much data in redis, check the size of
     *      values that have same key. If size is too big (over 300), delete data.
     *      To select which data to delete, use score, created by current time.
     * 5. Also, expire hour is 4.
     * </pre>
     * May throws exception of redis, or connection to redis database, that convert
     * from {@link RedisRepository}
     */
    @Test
    @DisplayName("increaseViewCount - success / view increase")
    void testIncreaseViewCount_success() {
        Long userId = 1L;
        Long postId = 100L;
        String postKey = String.valueOf(postId);
        String sortedSetKey = "views:" + userId;

        ValueDto.FindPost dto = ValueDto.FindPost.builder()
                .postId(postId)
                .userId(userId)
                .build();

        //기존에 조회한 기록이 없는 경우
        when(redisRepository.getRank(sortedSetKey, postKey))
                .thenReturn(null);
        //300개가 넘지 않음
        when(redisRepository.getSortedSetSize(sortedSetKey)).thenReturn(100L);

        postService.increaseViewCount(dto);

        verify(postRepository, times(1)).increaseView(postId);
        verify(redisRepository, times(1))
                .addToSortedSet(eq(sortedSetKey), eq(postKey), anyDouble());
        verify(redisRepository, never()).removeRangeFromSortedSet(eq(sortedSetKey), eq(0L), anyLong());
        verify(redisRepository, times(1)).setExpire(eq(sortedSetKey), anyLong());
    }

    @Test
    @DisplayName("increaseViewCount - success / already seen post")
    void testIncreaseViewCount_success_alreadySeenPost() {
        String username = "username";
        Long userId = 1L;
        Long postId = 100L;
        String postKey = String.valueOf(postId);
        String sortedSetKey = "views:" + userId;
        ValueDto.FindPost dto = ValueDto.FindPost.builder()
                .postId(postId)
                .userId(userId)
                .build();


        //기존에 조회한 기록이 존재하는 경우
        when(redisRepository.getRank(sortedSetKey, postKey)).thenReturn(184931L);

        postService.increaseViewCount(dto);

        verify(postRepository, never()).increaseView(anyLong());
        verify(redisRepository, times(1)).setExpire(eq(sortedSetKey), anyLong());
    }

    @Test
    @DisplayName("increaseViewCount - success / record over 300")
    void testIncreaseViewCount_success_recordOver300() {
        String username = "username";
        Long userId = 1L;
        Long postId = 100L;
        String postKey = String.valueOf(postId);
        String sortedSetKey = "views:" + userId;
        ValueDto.FindPost dto = ValueDto.FindPost.builder()
                .postId(postId)
                .userId(userId)
                .build();


        //기존에 조회한 기록이 없는 경우
        when(redisRepository.getRank(sortedSetKey, postKey))
                .thenReturn(null);
        //300개가 넘지 않음
        when(redisRepository.getSortedSetSize(sortedSetKey)).thenReturn(301L);

        postService.increaseViewCount(dto);

        verify(postRepository, times(1)).increaseView(postId);
        verify(redisRepository, times(1))
                .addToSortedSet(eq(sortedSetKey), eq(postKey), anyDouble());
        verify(redisRepository, times(1)).removeRangeFromSortedSet(eq(sortedSetKey), eq(0L), anyLong());
        verify(redisRepository, times(1)).setExpire(eq(sortedSetKey), anyLong());
    }

    //-+-+-+-+-+-+]] increaseLikeCount [[-+-+-+-+-+-+
    @Test
    @DisplayName("increaseLikeCount - success")
    void testIncreaseLikeCount_success() {
        Long userId = 1L;
        Long postId = 100L;
        LikesMapId likesMapId = LikesMapId.builder()
                .member(userId)
                .post(postId)
                .build();
        when(memberRepository.findById(userId)).thenReturn(Optional.of(mockMember));
        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));

        when(mockMember.getId()).thenReturn(userId);
        when(mockPost.getId()).thenReturn(postId);

        when(likesMapRepository.existsById(likesMapId)).thenReturn(false);

        ValueDto.FindPost dto = ValueDto.FindPost.builder()
                .postId(postId)
                .userId(userId)
                .build();

        postService.increaseLikesCount(dto);

        verify(likesMapRepository, times(1)).save(any(LikesMap.class));
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        verify(redisRepository, times(1)).increaseValue(captor.capture());
        String prefixValue = captor.getValue();
        assertThat(prefixValue).isEqualTo("post:likes:" + postId);
    }

    @Test
    @DisplayName("increaseLikeCount - success / already saved")
    void testIncreaseLikeCount_success_alreadySaved() {
        Long userId = 1L;
        Long postId = 100L;
        LikesMapId likesMapId = LikesMapId.builder()
                .member(userId)
                .post(postId)
                .build();

        when(memberRepository.findById(userId)).thenReturn(Optional.of(mockMember));
        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));

        when(mockMember.getId()).thenReturn(userId);
        when(mockPost.getId()).thenReturn(postId);

        when(likesMapRepository.existsById(likesMapId)).thenReturn(true);

        ValueDto.FindPost dto = ValueDto.FindPost.builder()
                .postId(postId)
                .userId(userId)
                .build();

        postService.increaseLikesCount(dto);

        verify(likesMapRepository, never()).save(any(LikesMap.class));
        verify(redisRepository, never()).increaseValue(anyString());
    }

    //-+-+-+-+-+-+]] decreaseLikeCount [[-+-+-+-+-+-+
    @Test
    @DisplayName("decreaseLikeCount - success")
    void testDecreaseLikeCount_success() {
        Long userId = 1L;
        Long postId = 100L;

        LikesMapId likesMapId = LikesMapId.builder()
                .member(userId)
                .post(postId)
                .build();

        ValueDto.FindPost dto = ValueDto.FindPost.builder()
                .postId(postId)
                .userId(userId)
                .build();
        postService.decreaseLikesCount(dto);
        verify(likesMapRepository, times(1)).deleteById(likesMapId);
        verify(redisRepository, times(1)).decreaseValue(anyString());
    }
    @Test
    @DisplayName("decreaseLikeCount - fail / not Likes before")
    void testDecreaseLikeCount_fail_notLikes() {

        Long userId = 1L;
        Long postId = 100L;
        ValueDto.FindPost dto = ValueDto.FindPost.builder()
                .postId(postId)
                .userId(userId)
                .build();
        doThrow(EmptyResultDataAccessException.class).when(likesMapRepository).deleteById(any(LikesMapId.class));

        assertThatThrownBy(() -> postService.decreaseLikesCount(dto))
                .isInstanceOf(UserBoardException.class)
                .hasMessageContaining("perhaps, user may not commit likes in DB")
                .extracting("errorCode")
                .isEqualTo(BoardErrorCode.NOT_EXISTING);

        verify(redisRepository, never()).decreaseValue(anyString());
    }

}