package com.onebucket.domain.boardManage.api;

import com.onebucket.domain.boardManage.dto.CreatePostDto;
import com.onebucket.domain.boardManage.entity.post.Post;
import com.onebucket.domain.boardManage.service.BoardService;
import com.onebucket.domain.boardManage.service.PostService;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    public ResponseEntity<Page<Post>> getPosts(Pageable pageable) {
        return ResponseEntity.ok(postService.getPosts(pageable));
    }

    @PostMapping
    public ResponseEntity<SuccessResponseDto> createPost(CreatePostDto dto) {
        String username = securityUtils.getCurrentUsername();

        if(boardService.isValidBoard(username, dto.getBoard())) {

        }

        return ResponseEntity.ok(new SuccessResponseDto("load"));
    }
}
