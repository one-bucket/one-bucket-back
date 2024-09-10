package com.onebucket.domain.boardManage.api;

import com.onebucket.domain.boardManage.dto.internal.board.GetBoardDto;
import com.onebucket.domain.boardManage.dto.internal.post.*;
import com.onebucket.domain.boardManage.dto.request.RequestCreatePostDto;
import com.onebucket.domain.boardManage.dto.response.ResponsePostDto;
import com.onebucket.domain.boardManage.service.PostService;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseWithIdDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-07-18        jack8              init create
 * </pre>
 */
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final SecurityUtils securityUtils;
    private final MemberService memberService;

    @GetMapping("/list/{boardId}")
    public ResponseEntity<Page<PostThumbnailDto>> getPostsByBoard(@PathVariable Long boardId, Pageable pageable) {
        String username = securityUtils.getCurrentUsername();

        securityUtils.isUserUniversityMatchingBoard(username, boardId);

        GetBoardDto getBoardDto = GetBoardDto.builder()
                .boardId(boardId)
                .pageable(pageable)
                .build();

        // dto 분리 일부로 안함
        Page<PostThumbnailDto> posts = postService.getPostsByBoard(getBoardDto);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ResponsePostDto> getPostById(@PathVariable Long postId) {
        String username = securityUtils.getCurrentUsername();

        GetPostDto getPostDto = GetPostDto.builder()
                .postId(postId)
                .username(username)
                .build();

        PostInfoDto postInfoDto = postService.getPost(getPostDto);
        ResponsePostDto response = ResponsePostDto.builder()
                .postId(postInfoDto.getPostId())
                .boardId(postInfoDto.getBoardId())
                .authorNickname(postInfoDto.getAuthorNickname())
                .createdDate(postInfoDto.getCreatedDate())
                .modifiedDate(postInfoDto.getModifiedDate())
                .comments(postInfoDto.getComments())
                .title(postInfoDto.getTitle())
                .text(postInfoDto.getText())
                .build();

        PostAuthorDto postAuthorDto = PostAuthorDto.builder()
                .postId(postId)
                .username(username)
                .build();
        postService.increaseViewCount(postAuthorDto);

        return ResponseEntity.ok(response);
    }

    @PostMapping
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
}
