package com.onebucket.domain.boardManage.api;

import com.onebucket.domain.boardManage.dto.internal.CreatePostDto;
import com.onebucket.domain.boardManage.dto.internal.DeletePostDto;
import com.onebucket.domain.boardManage.dto.internal.GetBoardDto;
import com.onebucket.domain.boardManage.dto.request.RequestCreatePostDto;
import com.onebucket.domain.boardManage.entity.post.Post;
import com.onebucket.domain.boardManage.service.BoardService;
import com.onebucket.domain.boardManage.service.PostService;
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
    private final BoardService boardService;
    private final SecurityUtils securityUtils;

    @GetMapping("/{boardId}")
    public ResponseEntity<Page<Post>> getPostsByBoard(@PathVariable Long boardId, Pageable pageable) {
        String username = securityUtils.getCurrentUsername();

        securityUtils.isUserUniversityMatchingBoard(username, boardId);

        GetBoardDto getBoardDto = GetBoardDto.builder()
                .boardId(boardId)
                .pageable(pageable)
                .build();

        Page<Post> posts = postService.getPostsByBoard(getBoardDto);
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<SuccessResponseWithIdDto> createPost(@RequestBody @Valid RequestCreatePostDto dto) {
        String username = securityUtils.getCurrentUsername();

        CreatePostDto createPostDto = CreatePostDto.builder()
                .boardId(dto.getBoardId())
                .text(dto.getText())
                .title(dto.getTitle())
                .username(username)
                .build();

        Long savedId = postService.createPost(createPostDto);
        return ResponseEntity.ok(new SuccessResponseWithIdDto("success create post", savedId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponseWithIdDto> deletePost(@PathVariable Long id) {
        String username = securityUtils.getCurrentUsername();

        DeletePostDto deletePostDto = DeletePostDto.builder()
                .id(id)
                .username(username)
                .build();

        postService.deletePost(deletePostDto);

        return ResponseEntity.ok(new SuccessResponseWithIdDto("success delete post", id));
    }
}
