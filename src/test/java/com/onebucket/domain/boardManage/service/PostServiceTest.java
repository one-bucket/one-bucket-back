package com.onebucket.domain.boardManage.service;

import com.onebucket.domain.boardManage.dao.BoardRepository;
import com.onebucket.domain.boardManage.dao.PostRepository;
import com.onebucket.domain.boardManage.dto.internal.post.CreatePostDto;
import com.onebucket.domain.boardManage.entity.Board;
import com.onebucket.domain.boardManage.entity.BoardType;
import com.onebucket.domain.boardManage.entity.post.Post;
import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.universityManage.domain.University;
import com.onebucket.global.exceptionManage.customException.boardManageException.BoardManageException;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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


}