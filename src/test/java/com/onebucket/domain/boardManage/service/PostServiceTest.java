package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.BoardRepository;
import com.onebucket.domain.boardManage.dao.CommentRepository;
import com.onebucket.domain.boardManage.dao.PostRepository;
import com.onebucket.domain.boardManage.dto.internal.comment.CreateCommentDto;
import com.onebucket.domain.boardManage.dto.internal.post.CreatePostDto;
import com.onebucket.domain.boardManage.dto.internal.post.DeletePostDto;
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
        CreatePostDto dto = CreatePostDto.builder()
                .username(username)
                .boardId(boardId)
                .univId(univId)
                .build();
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
        when(postRepository.save(any(Post.class))).thenReturn(mockPost);
        when(boardRepository.findById(1L)).thenReturn(Optional.of(mockBoard));
        when(mockBoard.getUniversity()).thenReturn(mockUniversity);
        when(mockUniversity.getId()).thenReturn(1L);
        when(mockPost.getId()).thenReturn(100L);

        Long postId = postService.createPost(dto);

        verify(postRepository, times(1)).save(any(Post.class));
        assertThat(postId).isEqualTo(100L);
    }

    @Test
    @DisplayName("createPost - fail /invalid university")
    void testCreatePost_fail_invalid_university() {
        String username = "username";
        Long univId = 1L;
        Long boardId = 1L;
        CreatePostDto dto = CreatePostDto.builder()
                .username(username)
                .boardId(boardId)
                .univId(univId)
                .build();
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
        when(boardRepository.findById(1L)).thenReturn(Optional.of(mockBoard));
        when(mockBoard.getUniversity()).thenReturn(mockUniversity);
        when(mockUniversity.getId()).thenReturn(2L);

        assertThatThrownBy(() -> postService.createPost(dto))
                .isInstanceOf(AuthenticationException.class)
                .extracting("errorCode")
                .isEqualTo(AuthenticationErrorCode.INVALID_SUBMIT);

        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    @DisplayName("createPost - fail / unknown board")
    void testCreateBoard_fail_unknownBoard() {
        String username = "username";
        Long univId = 1L;
        Long boardId = 1L;
        CreatePostDto dto = CreatePostDto.builder()
                .username(username)
                .boardId(boardId)
                .univId(univId)
                .build();
        when(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember));
        when(boardRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.createPost(dto))
                .isInstanceOf(BoardManageException.class)
                .extracting("errorCode")
                .isEqualTo(BoardErrorCode.UNKNOWN_BOARD);

        verify(postRepository, never()).save(any(Post.class));
    }

    //-+-+-+-+-+-+]] deletePost [[-+-+-+-+-+-+
    @Test
    @DisplayName("deletePost - success")
    void testDeletePost_success() {
        Long memberId = 100L;
        Long postId = 1L;
        DeletePostDto dto = DeletePostDto.builder()
                .id(postId)
                .memberId(memberId)
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(mockPost));
        when(mockPost.getAuthor()).thenReturn(mockMember);
        when(mockMember.getId()).thenReturn(memberId);

        postService.deletePost(dto);
        verify(postRepository, times(1)).delete(any(Post.class));
    }

    @Test
    @DisplayName("deletePost - fail / unknown post")
    void testDeletePost_fail_unknownPost() {
        Long memberId = 100L;
        Long postId = 1L;
        DeletePostDto dto = DeletePostDto.builder()
                .id(postId)
                .memberId(memberId)
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

        Long memberId = 100L;
        Long postId = 1L;
        DeletePostDto dto = DeletePostDto.builder()
                .id(postId)
                .memberId(memberId)
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
        Long memberId = 100L;
        Long postId = 1L;
        DeletePostDto dto = DeletePostDto.builder()
                .id(postId)
                .memberId(memberId)
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

    //-+-+-+-+-+-+]] increaseViewCount [[-+-+-+-+-+-+
}