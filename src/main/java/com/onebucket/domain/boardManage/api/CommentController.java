package com.onebucket.domain.boardManage.api;

import com.onebucket.domain.boardManage.dto.internal.comment.CreateCommentDto;
import com.onebucket.domain.boardManage.dto.request.RequestCreateCommentDto;
import com.onebucket.domain.boardManage.service.PostService;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <br>package name   : com.onebucket.domain.boardManage.api
 * <br>file name      : CommentController
 * <br>date           : 2024-08-15
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
 * 2024-08-15        jack8              init create
 * </pre>
 */

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final SecurityUtils securityUtils;
    private final PostService postService;

    @PostMapping
    public ResponseEntity<SuccessResponseDto> createComment(@RequestBody @Valid RequestCreateCommentDto dto) {
        String username = securityUtils.getCurrentUsername();
        Long univId = securityUtils.getUnivId(username);

        CreateCommentDto createCommentDto = CreateCommentDto.builder()
                .postId(dto.getPostId())
                .parentCommentId(dto.getParentCommentId())
                .text(dto.getText())
                .username(username)
                .build();

        postService.addCommentToPost(createCommentDto);

        return ResponseEntity.ok(new SuccessResponseDto("success create comment"));

    }
}
