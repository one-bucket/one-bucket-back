package com.onebucket.domain.boardManage.api;

import com.onebucket.domain.boardManage.dto.internal.board.GetBoardDto;
import com.onebucket.domain.boardManage.dto.internal.post.*;
import com.onebucket.domain.boardManage.dto.request.RequestCreateMarketPostDto;
import com.onebucket.domain.boardManage.dto.response.ResponseMarketPostDto;
import com.onebucket.domain.boardManage.dto.response.ResponsePostDto;
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

    public MarketPostController(MarketPostService postService, SecurityUtils securityUtils, MemberService memberService, BoardService boardService) {
        super(postService, securityUtils, memberService, boardService);
    }

    @PreAuthorize("@authorizationService.isUserCanAccessBoard(#dto.boardId)")
    @PostMapping("/create")
    public ResponseEntity<SuccessResponseWithIdDto> createPost(@RequestBody @Valid RequestCreateMarketPostDto dto) {

        String type = boardService.getType(dto.getBoardId());
        System.out.println("type of market is " + type);
        if(!type.equals("marketPost")) {
            throw new UserBoardException(BoardErrorCode.MISMATCH_POST_AND_BOARD);
        }

        String username = securityUtils.getCurrentUsername();
        Long univId = securityUtils.getUnivId(username);

        CreateMarketPostDto createMarketPostDto = CreateMarketPostDto.builder()
                .boardId(dto.getBoardId())
                .text(dto.getText())
                .title(dto.getTitle())
                .username(username)
                .univId(univId)
                .item(dto.getItem())
                .location(dto.getLocation())
                .wanted(dto.getWanted())
                .build();

        Long savedId = postService.createPost(createMarketPostDto);
        return ResponseEntity.ok(new SuccessResponseWithIdDto("success create post", savedId));
    }

    @Override
    protected ResponseEntity<? extends ResponsePostDto> getPostInternal(GetPostDto dto) {
        MarketPostInfoDto marketPostInfoDto = (MarketPostInfoDto) postService.getPost(dto);

        Long savedInRedisLikes = postService.getLikesInRedis(marketPostInfoDto.getPostId());

        Long likes = marketPostInfoDto.getLikes() + savedInRedisLikes;

        Long userId = memberService.usernameToId(dto.getUsername());

        PostAuthorDto postAuthorDto = PostAuthorDto.builder()
                .userId(userId)
                .postId(dto.getPostId())
                .build();
        boolean isUserAlreadyLikes = postService.isUserLikesPost(postAuthorDto);

        ResponseMarketPostDto response = ResponseMarketPostDto.builder()
                .postId(marketPostInfoDto.getPostId())
                .boardId(marketPostInfoDto.getBoardId())
                .authorNickname(marketPostInfoDto.getAuthorNickname())
                .createdDate(marketPostInfoDto.getCreatedDate())
                .modifiedDate(marketPostInfoDto.getModifiedDate())
                .comments(marketPostInfoDto.getComments())
                .title(marketPostInfoDto.getTitle())
                .text(marketPostInfoDto.getText())
                .item(marketPostInfoDto.getItem())
                .wanted(marketPostInfoDto.getWanted())
                .location(marketPostInfoDto.getLocation())
                .likes(likes)
                .views(marketPostInfoDto.getViews())
                .isUserAlreadyLikes(isUserAlreadyLikes)
                .build();

        increaseViewCountInternal(postAuthorDto);

        return ResponseEntity.ok(response);
    }

    @Override
    protected ResponseEntity<Page<? extends PostThumbnailDto>> getPostByBoardInternal(GetBoardDto getBoardDto) {
        Page<MarketPostThumbnailDto> posts = postService.getPostsByBoard(getBoardDto)
                        .map(post -> (MarketPostThumbnailDto) post);

        posts.forEach(post -> {
            Long commentCount = (Long) postService.getCommentCount(post.getPostId());
            post.setCommentsCount(commentCount);
            post.setLikes(post.getLikes() + postService.getLikesInRedis(post.getPostId()));
        });
        return ResponseEntity.ok(posts);
    }
}
