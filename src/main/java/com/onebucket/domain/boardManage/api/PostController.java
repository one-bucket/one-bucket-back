package com.onebucket.domain.boardManage.api;

import com.onebucket.domain.boardManage.dto.internal.board.GetBoardDto;
import com.onebucket.domain.boardManage.dto.internal.post.*;
import com.onebucket.domain.boardManage.dto.request.RequestCreatePostDto;
import com.onebucket.domain.boardManage.dto.response.ResponsePostDto;
import com.onebucket.domain.boardManage.entity.post.Post;
import com.onebucket.domain.boardManage.service.BoardService;
import com.onebucket.domain.boardManage.service.PostService;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.global.exceptionManage.customException.boardManageException.UserBoardException;
import com.onebucket.global.exceptionManage.errorCode.BoardErrorCode;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseWithIdDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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
public class PostController extends AbstractPostController<Post, PostService>{

    public PostController(PostService postService, SecurityUtils securityUtils, MemberService memberService, BoardService boardService) {
        super(postService, securityUtils, memberService, boardService);
    }

    @Override
    protected ResponseEntity<? extends ResponsePostDto> getPostInternal(GetPostDto dto) {

        PostInfoDto postInfoDto = postService.getPost(dto);

        Long savedInRedisLikes = postService.getLikesInRedis(postInfoDto.getPostId());
        Long likes = postInfoDto.getLikes() + savedInRedisLikes;

        Long userId = memberService.usernameToId(dto.getUsername());

        PostAuthorDto postAuthorDto = PostAuthorDto.builder()
                .userId(userId)
                .postId(dto.getPostId())
                .build();
        boolean isUserAlreadyLikes = postService.isUserLikesPost(postAuthorDto);

        ResponsePostDto response = ResponsePostDto.builder()
                .postId(postInfoDto.getPostId())
                .boardId(postInfoDto.getBoardId())
                .authorNickname(postInfoDto.getAuthorNickname())
                .createdDate(postInfoDto.getCreatedDate())
                .modifiedDate(postInfoDto.getModifiedDate())
                .comments(postInfoDto.getComments())
                .title(postInfoDto.getTitle())
                .text(postInfoDto.getText())
                .likes(likes)
                .views(postInfoDto.getViews())
                .isUserAlreadyLikes(isUserAlreadyLikes)
                .build();

        increaseViewCountInternal(postAuthorDto);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@authorizationService.isUserCanAccessBoard(#dto.boardId)")
    @PostMapping("/create")
    public ResponseEntity<SuccessResponseWithIdDto> createPost(@RequestBody @Valid RequestCreatePostDto dto) {
        String type = boardService.getType(dto.getBoardId());

        if(!type.equals("Post")) {
            throw new UserBoardException(BoardErrorCode.MISMATCH_POST_AND_BOARD);
        }
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

    @Override
    protected ResponseEntity<Page<? extends PostThumbnailDto>> getPostByBoardInternal(GetBoardDto getBoardDto) {
        Page<PostThumbnailDto> posts = postService.getPostsByBoard(getBoardDto);

        posts.forEach(post -> {
            Long commentCount = (Long) postService.getCommentCount(post.getPostId());
            post.setCommentsCount(commentCount);
            post.setLikes(post.getLikes() + postService.getLikesInRedis(post.getPostId()));
        });
        return ResponseEntity.ok(posts);
    }
}
