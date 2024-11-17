package com.onebucket.domain.boardManage.api;

import com.onebucket.domain.boardManage.dto.postDto.PostDto;
import com.onebucket.domain.boardManage.dto.parents.ValueDto;
import com.onebucket.domain.boardManage.service.BoardService;
import com.onebucket.domain.boardManage.service.PostService;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.tradeManage.dto.TradeDto;
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
        Long userId = securityUtils.getUserId();
        Long univId = securityUtils.getUnivId();

        PostDto.Create createPostDto = PostDto.Create.builder()
                .boardId(dto.getBoardId())
                .text(dto.getText())
                .title(dto.getTitle())
                .userId(userId)
                .univId(univId)
                .build();

        Long savedId = postService.createPost(createPostDto);
        return ResponseEntity.ok(new SuccessResponseWithIdDto("success create post", savedId));
    }

    @PreAuthorize("@authorizationService.isUserCanAccessPost(#dto.postId)")
    @PostMapping("/update")
    public ResponseEntity<SuccessResponseWithIdDto> updatePost(@RequestBody @Valid PostDto.Update dto) {

        //MarketPost update
        postService.updatePost(dto);

        return ResponseEntity.ok(new SuccessResponseWithIdDto("success update post", dto.getPostId()));
    }

    @Override
    protected ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostByBoardInternal(ValueDto.PageablePost getBoardDto) {
        Page<PostDto.Thumbnail> posts = postService.getPostsByBoard(getBoardDto);

        posts.forEach(this::addLikeAndCommentInfoOnThumbnail);
        return ResponseEntity.ok(posts);
    }

    @Override
    protected ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostsBySearchInternal(ValueDto.SearchPageablePost dto) {
        Page<PostDto.Thumbnail> posts = postService.getSearchResult(dto);

        posts.forEach(this::addLikeAndCommentInfoOnThumbnail);
        return ResponseEntity.ok(posts);
    }

    private void addLikeAndCommentInfoOnThumbnail(PostDto.Thumbnail dto) {
        Long commentCount = postService.getCommentCount(dto.getPostId());
        dto.setCommentsCount(commentCount);
        dto.setLikes(dto.getLikes() + postService.getLikesInRedis(dto.getPostId()));
    }
}
