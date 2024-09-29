package com.onebucket.domain.boardManage.api;

import com.onebucket.domain.boardManage.dto.internal.board.GetBoardDto;
import com.onebucket.domain.boardManage.dto.parents.MarketPostDto;
import com.onebucket.domain.boardManage.dto.parents.PostDto;
import com.onebucket.domain.boardManage.dto.parents.ValueDto;
import com.onebucket.domain.boardManage.entity.post.MarketPost;
import com.onebucket.domain.boardManage.service.BoardService;
import com.onebucket.domain.boardManage.service.MarketPostService;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.global.exceptionManage.customException.boardManageException.UserBoardException;
import com.onebucket.global.exceptionManage.errorCode.BoardErrorCode;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseWithIdDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <br>package name   : com.onebucket.domain.boardManage.api
 * <br>file name      : MarketPostController
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

@RestController
@RequestMapping("/market-post")
public class MarketPostController extends AbstractPostController<MarketPost, MarketPostService> {


    public MarketPostController(MarketPostService postService, SecurityUtils securityUtils,
                                MemberService memberService, BoardService boardService) {
        super(postService, securityUtils, memberService, boardService);
    }

    @PreAuthorize("@authorizationService.isUserCanAccessBoard(#dto.boardId)")
    @PostMapping("/create")
    public ResponseEntity<SuccessResponseWithIdDto> createPost(@RequestBody @Valid MarketPostDto.RequestCreate dto) {

        String type = boardService.getType(dto.getBoardId());
        if(!type.equals("marketPost")) {
            throw new UserBoardException(BoardErrorCode.MISMATCH_POST_AND_BOARD);
        }

        String username = securityUtils.getCurrentUsername();
        Long univId = securityUtils.getUnivId(username);

        MarketPostDto.Create createMarketPostDto = MarketPostDto.Create.builder()
                .boardId(dto.getBoardId())
                .text(dto.getText())
                .title(dto.getTitle())
                .username(username)
                .univId(univId)
                .build();

        Long savedId = postService.createPost(createMarketPostDto);
        return ResponseEntity.ok(new SuccessResponseWithIdDto("success create post", savedId));
    }

    @Override
    protected ResponseEntity<? extends PostDto.ResponseInfo> getPostInternal(ValueDto.FindPost dto) {

        ValueDto.GetPost getPost = ValueDto.GetPost.of(dto);
        MarketPostDto.Info marketPostInfoDto = (MarketPostDto.Info) postService.getPost(getPost);

        Long savedInRedisLikes = postService.getLikesInRedis(marketPostInfoDto.getPostId());

        Long likes = marketPostInfoDto.getLikes() + savedInRedisLikes;

        boolean isUserAlreadyLikes = postService.isUserLikesPost(dto);

        MarketPostDto.ResponseInfo response = MarketPostDto.ResponseInfo.of(marketPostInfoDto);
        response.setLikes(likes);
        response.setUserAlreadyLikes(isUserAlreadyLikes);

        increaseViewCountInternal(dto);

        return ResponseEntity.ok(response);
    }

    @Override
    protected ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostByBoardInternal(GetBoardDto getBoardDto) {
        Page<MarketPostDto.Thumbnail> posts = postService.getPostsByBoard(getBoardDto)
                        .map(post -> (MarketPostDto.Thumbnail) post);

        posts.forEach(post -> {
            Long commentCount = (Long) postService.getCommentCount(post.getPostId());
            post.setCommentsCount(commentCount);
            post.setLikes(post.getLikes() + postService.getLikesInRedis(post.getPostId()));
        });
        return ResponseEntity.ok(posts);
    }
}
