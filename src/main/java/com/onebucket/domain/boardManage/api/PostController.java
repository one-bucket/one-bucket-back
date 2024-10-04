package com.onebucket.domain.boardManage.api;

import com.onebucket.domain.boardManage.dto.parents.PostDto;
import com.onebucket.domain.boardManage.dto.parents.ValueDto;
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
public class PostController extends AbstractPostController<PostService>{

    public PostController(PostService postService, SecurityUtils securityUtils, MemberService memberService, BoardService boardService) {
        super(postService, securityUtils, memberService, boardService);
    }

    @Override
    protected ResponseEntity<? extends PostDto.ResponseInfo> getPostInternal(ValueDto.FindPost dto) {

        ValueDto.GetPost getPost = ValueDto.GetPost.of(dto);

        PostDto.Info postInfoDto = postService.getPost(getPost);

        Long savedInRedisLikes = postService.getLikesInRedis(postInfoDto.getPostId());
        Long likes = postInfoDto.getLikes() + savedInRedisLikes;

        boolean isUserAlreadyLikes = postService.isUserLikesPost(dto);

        PostDto.ResponseInfo response = PostDto.ResponseInfo.of(postInfoDto);
        response.setLikes(likes);
        response.setUserAlreadyLikes(isUserAlreadyLikes);

        increaseViewCountInternal(dto);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@authorizationService.isUserCanAccessBoard(#dto.boardId)")
    @PostMapping("/create")
    public ResponseEntity<SuccessResponseWithIdDto> createPost(@RequestBody @Valid PostDto.RequestCreate dto) {
        String type = boardService.getType(dto.getBoardId());

        if(!type.equals("post")) {
            throw new UserBoardException(BoardErrorCode.MISMATCH_POST_AND_BOARD);
        }
        String username = securityUtils.getCurrentUsername();
        Long univId = securityUtils.getUnivId(username);

        PostDto.Create createPostDto = PostDto.Create.builder()
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
    protected ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostByBoardInternal(ValueDto.PageablePost getBoardDto) {
        Page<PostDto.Thumbnail> posts = postService.getPostsByBoard(getBoardDto);

        posts.forEach(post -> {
            Long commentCount = (Long) postService.getCommentCount(post.getPostId());
            post.setCommentsCount(commentCount);
            post.setLikes(post.getLikes() + postService.getLikesInRedis(post.getPostId()));
        });
        return ResponseEntity.ok(posts);
    }
}
