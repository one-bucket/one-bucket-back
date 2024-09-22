package com.onebucket.domain.boardManage.api;

import com.onebucket.domain.boardManage.dto.internal.board.GetBoardDto;
import com.onebucket.domain.boardManage.dto.internal.post.*;
import com.onebucket.domain.boardManage.dto.response.ResponsePostDto;
import com.onebucket.domain.boardManage.entity.post.Post;
import com.onebucket.domain.boardManage.service.BasePostService;
import com.onebucket.domain.boardManage.service.BoardService;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseDto;
import com.onebucket.global.utils.SuccessResponseWithIdDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <br>package name   : com.onebucket.domain.boardManage.api
 * <br>file name      : AbstractPostController
 * <br>date           : 2024-09-22
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

@RequiredArgsConstructor
public abstract class AbstractPostController<T extends Post, S extends BasePostService> {
    protected final S postService;
    protected final SecurityUtils securityUtils;
    protected final MemberService memberService;
    protected final BoardService boardService;

    @GetMapping("/list/{boardId}")
    @PreAuthorize("@authorizationService.isUserCanAccessBoard(#boardId)")
    public ResponseEntity<Page<PostThumbnailDto>> getPostsByBoard(@PathVariable Long boardId, Pageable pageable) {
        GetBoardDto getBoardDto = GetBoardDto.builder()
                .boardId(boardId)
                .pageable(pageable)
                .build();

        // dto 분리 일부로 안함
        Page<PostThumbnailDto> posts = postService.getPostsByBoard(getBoardDto);
        posts.forEach(post -> {
            Long commentCount = (Long) postService.getCommentCount(post.getPostId());
            post.setCommentsCount(commentCount);
            post.setLikes(post.getLikes() + postService.getLikesInRedis(post.getPostId()));
        });
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    @PreAuthorize("@authorizationService.isUserCanAccessPost(#postId)")
    public ResponseEntity<? extends ResponsePostDto> getPostById(@PathVariable Long postId) {
        String username = securityUtils.getCurrentUsername();
        Long userId = memberService.usernameToId(username);

        GetPostDto getPostDto = GetPostDto.builder()
                .postId(postId)
                .username(username)
                .build();

        return getPostInternal(getPostDto);
    }

    protected abstract ResponseEntity<? extends ResponsePostDto> getPostInternal(GetPostDto dto);

    protected void increaseViewCountInternal(PostAuthorDto postAuthorDto) {

        postService.increaseViewCount(postAuthorDto);
    }
    @DeleteMapping("/{postId}")
    public ResponseEntity<SuccessResponseWithIdDto> deletePost(@PathVariable Long postId) {
        String username = securityUtils.getCurrentUsername();
        Long userId = memberService.usernameToId(username);

        DeletePostDto deletePostDto = DeletePostDto.builder()
                .id(postId)
                .memberId(userId)
                .build();

        System.out.println("in time 1");
        postService.deletePost(deletePostDto);

        return ResponseEntity.ok(new SuccessResponseWithIdDto("success delete post", postId));
    }

    @PostMapping("/{postId}/like")
    @PreAuthorize("@authorizationService.isUserCanAccessPost(#postId)")
    public ResponseEntity<SuccessResponseDto> addLikes(@PathVariable Long postId) {
        String username = securityUtils.getCurrentUsername();
        Long userId = memberService.usernameToId(username);
        PostAuthorDto postAuthorDto = PostAuthorDto.builder()
                .userId(userId)
                .postId(postId)
                .build();
        postService.increaseLikesCount(postAuthorDto);

        return ResponseEntity.ok(new SuccessResponseDto("success add likes"));
    }

    @DeleteMapping("/{postId}/like")
    @PreAuthorize("@authorizationService.isUserCanAccessPost(#postId)")
    public ResponseEntity<SuccessResponseDto> deleteLikes(@PathVariable Long postId) {
        String username = securityUtils.getCurrentUsername();
        Long userId = memberService.usernameToId(username);
        PostAuthorDto postAuthorDto = PostAuthorDto.builder()
                .userId(userId)
                .postId(postId)
                .build();
        postService.decreaseLikesCount(postAuthorDto);

        return ResponseEntity.ok(new SuccessResponseDto("success delete likes"));
    }



}
