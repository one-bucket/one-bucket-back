package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.BoardRepository;
import com.onebucket.domain.boardManage.dao.CommentRepository;
import com.onebucket.domain.boardManage.dao.PostRepository;
import com.onebucket.domain.boardManage.dto.internal.comment.CreateCommentDto;
import com.onebucket.domain.boardManage.dto.parents.PostDto;
import com.onebucket.domain.boardManage.dto.parents.ValueDto;
import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.boardManage.entity.BoardType;
import com.onebucket.domain.boardManage.entity.Comment;
import com.onebucket.domain.boardManage.entity.post.Post;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.universityManage.domain.University;
import com.onebucket.global.exceptionManage.customException.boardManageException.BoardManageException;
import com.onebucket.global.exceptionManage.customException.boardManageException.UserBoardException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.BoardErrorCode;
import com.onebucket.global.redis.RedisRepository;
import com.onebucket.global.utils.SecurityUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    @InjectMocks
    private PostServiceImpl postService;

    //-+-+-+-+-+-+]] createPost [[-+-+-+-+-+-+
    @Test
    @DisplayName("createPost - success")
    void testCreatePost_success() {

        String username = "username";
        Long univId = 1L;
        Long boardId = 1L;
        PostDto.Create createDto = PostDto.Create.builder()
                .boardId(boardId)
                .univId(univId)
                .username(username)
                .title("title")
                .text("text")
                .build();
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
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
        String username = "username";
        Long univId = 1L;
        Long boardId = 1L;
        PostDto.Create createDto = PostDto.Create.builder()
                .boardId(boardId)
                .univId(univId)
                .username(username)
                .title("title")
                .text("text")
                .build();
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
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
        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));
        when(mockPost.getComments()).thenReturn(List.of(mockComment));
        when(mockComment.getId()).thenReturn(commentId);

        ValueDto.FindComment dto = ValueDto.FindComment.builder()
                .postId(postId)
                .commentId(commentId)
                .build();

        postService.deleteCommentFromPost(dto);

        verify(postRepository, times(1)).save(mockPost);
    }

    @Test
    @DisplayName("deleteCommentFromPost - fail / No post to delete")
    void testDeleteCommentFromPost_fail_noPostToDelete() {
        Long postId = 1L;
        Long commentId = 10L;
        Long savedCommentId = 11L;

        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));
        when(mockPost.getComments()).thenReturn(List.of(mockComment));
        when(mockComment.getId()).thenReturn(savedCommentId);

        ValueDto.FindComment dto = ValueDto.FindComment.builder()
                .postId(1L)
                .commentId(commentId)
                .build();
        assertThatThrownBy(() -> postService.deleteCommentFromPost(dto))
                .isInstanceOf(UserBoardException.class)
                .extracting(("errorCode"))
                .isEqualTo(BoardErrorCode.UNKNOWN_COMMENT);
        verify(postRepository, never()).save(mockPost);
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
                .extracting("id")
                .containsAll(List.of(1L, 2L,3L, 4L, 5L));
    }



    //-+-+-+-+-+-+]] increaseViewCount [[-+-+-+-+-+-+
    @Test
    @DisplayName("increaseViewCount - success / view increase")
    void testIncreaseViewCount_success() {
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
}