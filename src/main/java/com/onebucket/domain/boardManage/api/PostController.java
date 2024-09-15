package com.onebucket.domain.boardManage.api;

import com.onebucket.domain.boardManage.dto.internal.board.GetBoardDto;
import com.onebucket.domain.boardManage.dto.internal.post.*;
import com.onebucket.domain.boardManage.dto.request.RequestCreatePostDto;
import com.onebucket.domain.boardManage.dto.response.ResponsePostDto;
import com.onebucket.domain.boardManage.service.PostService;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseDto;
import com.onebucket.global.utils.SuccessResponseWithIdDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <br>package name   : com.onebucket.domain.boardManage.api
 * <br>file name      : PostController
 * <br>date           : 2024-07-18
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
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final SecurityUtils securityUtils;
    private final MemberService memberService;

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
            Long commentCount = postService.getCommentCount(post.getPostId());
            post.setCommentsCount(commentCount);
        });
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    @PreAuthorize("@authorizationService.isUserCanAccessPost(#postId)")
    public ResponseEntity<ResponsePostDto> getPostById(@PathVariable Long postId) {
        String username = securityUtils.getCurrentUsername();
        Long userId = memberService.usernameToId(username);

        GetPostDto getPostDto = GetPostDto.builder()
                .postId(postId)
                .username(username)
                .build();

        PostInfoDto postInfoDto = postService.getPost(getPostDto);

        ResponsePostDto response = mapResponsePostDto(postInfoDto);

        PostAuthorDto postAuthorDto = PostAuthorDto.builder()
                .postId(postId)
                .userId(userId)
                .build();
        postService.increaseViewCount(postAuthorDto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<SuccessResponseWithIdDto> deletePost(@PathVariable Long postId) {
        String username = securityUtils.getCurrentUsername();
        Long userId = memberService.usernameToId(username);

        DeletePostDto deletePostDto = DeletePostDto.builder()
                .id(postId)
                .memberId(userId)
                .build();

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

    @PreAuthorize("@authorizationService.isUserCanAccessBoard(#dto.boardId)")
    @PostMapping("/create/{boardId}")
    public ResponseEntity<SuccessResponseWithIdDto> createPost(@RequestBody @Valid RequestCreatePostDto dto) {
        String username = securityUtils.getCurrentUsername();
        Long univId = securityUtils.getUnivId(username);

        CreatePostDto createPostDto = CreatePostDto.builder()
                .boardId(dto.getBoardId())
                .text(dto.getText())
                .title(dto.getTitle())
                .username(username)
                .univId(univId)
                .build();

        Long savedId = postService.createPost(createPostDto);
        return ResponseEntity.ok(new SuccessResponseWithIdDto("success create post", savedId));
    }


    private ResponsePostDto mapResponsePostDto(PostInfoDto postInfoDto) {
        return ResponsePostDto.builder()
                .postId(postInfoDto.getPostId())
                .boardId(postInfoDto.getBoardId())
                .authorNickname(postInfoDto.getAuthorNickname())
                .createdDate(postInfoDto.getCreatedDate())
                .modifiedDate(postInfoDto.getModifiedDate())
                .comments(postInfoDto.getComments())
                .title(postInfoDto.getTitle())
                .text(postInfoDto.getText())
                .build();
    }

}
