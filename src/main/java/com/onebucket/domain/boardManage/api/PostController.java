package com.onebucket.domain.boardManage.api;

import com.onebucket.domain.boardManage.dto.postDto.PostDto;
import com.onebucket.domain.boardManage.dto.postDto.PostKeyDto;
import com.onebucket.domain.boardManage.service.BoardService;
import com.onebucket.domain.boardManage.service.postService.PostService;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.global.exceptionManage.customException.boardManageException.UserBoardException;
import com.onebucket.global.exceptionManage.errorCode.BoardErrorCode;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseWithIdDto;
import jakarta.validation.Valid;
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
public class PostController extends AbstractPostController<PostService>{

    public PostController(PostService postService,
                          SecurityUtils securityUtils,
                          MemberService memberService,
                          BoardService boardService) {
        super(postService, securityUtils, memberService, boardService);
    }

    @Override
    protected ResponseEntity<? extends PostDto.ResponseInfo> getPostInternal(PostKeyDto.UserPost dto) {
        PostDto.Info info = postService.getPost(dto);

        PostDto.ResponseInfo response = convertInfoToResponse(info, dto);
        return ResponseEntity.ok(response);
    }

    @Override
    protected ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostsBySearchInternal(PostKeyDto.SearchPage dto) {
        Page<PostDto.Thumbnail> posts = postService.getSearchResult(dto)
                        .map(this::convertInternalThumbnailToThumbnail);
        return ResponseEntity.ok(posts);
    }

    @Override
    protected ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostByBoardInternal(PostKeyDto.BoardPage dto) {
        Page<PostDto.Thumbnail> posts = postService.getPostsByBoard(dto)
                .map(this::convertInternalThumbnailToThumbnail);
        return ResponseEntity.ok(posts);
    }

    @Override
    protected ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostByAuthorInternal(PostKeyDto.AuthorPage dto) {
        Page<PostDto.Thumbnail> posts = postService.getPostByAuthorId(dto)
                .map(this::convertInternalThumbnailToThumbnail);

        return ResponseEntity.ok(posts);
    }


    @PreAuthorize("@authorizationService.isUserCanAccessBoard(#dto.boardId)")
    @PostMapping("/create")
    public ResponseEntity<SuccessResponseWithIdDto> createPost(@RequestBody @Valid PostDto.RequestCreate dto) {
        String type = boardService.getType(dto.getBoardId());

        if(!type.equals("post")) {
            throw new UserBoardException(BoardErrorCode.MISMATCH_POST_AND_BOARD);
        }
        Long userId = securityUtils.getUserId();

        PostDto.Create createPostDto = PostDto.Create.builder()
                .boardId(dto.getBoardId())
                .text(dto.getText())
                .title(dto.getTitle())
                .userId(userId)
                .build();

        Long savedId = postService.createPost(createPostDto);
        return ResponseEntity.ok(new SuccessResponseWithIdDto("success create post", savedId));
    }

    @PreAuthorize("@authorizationService.isUserCanAccessPost(#dto.postId)")
    @PostMapping("/update")
    public ResponseEntity<SuccessResponseWithIdDto> updatePost(@RequestBody @Valid PostDto.Update dto) {

        postService.updatePost(dto);

        return ResponseEntity.ok(new SuccessResponseWithIdDto("success update post", dto.getPostId()));
    }

    @GetMapping("/list/likes")
    public ResponseEntity<Page<PostDto.Thumbnail>> getLikesPost(Pageable pageable) {
        Long userId = securityUtils.getUserId();
        PostKeyDto.UserPage userPage = PostKeyDto.UserPage.builder()
                .userId(userId)
                .pageable(pageable)
                .build();
        Page<PostDto.Thumbnail> response = postService.getLikePost(userPage).map(this::convertInternalThumbnailToThumbnail);

        return ResponseEntity.ok(response);
    }
}
