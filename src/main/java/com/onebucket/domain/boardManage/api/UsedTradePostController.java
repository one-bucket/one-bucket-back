package com.onebucket.domain.boardManage.api;

import com.onebucket.domain.boardManage.dto.postDto.PostDto;
import com.onebucket.domain.boardManage.dto.postDto.PostKeyDto;
import com.onebucket.domain.boardManage.dto.postDto.UsedTradePostDto;
import com.onebucket.domain.boardManage.service.BoardService;
import com.onebucket.domain.boardManage.service.postService.UsedTradePostService;
import com.onebucket.domain.chatManager.service.ChatRoomService;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.tradeManage.dto.UsedTradeDto;
import com.onebucket.domain.tradeManage.service.UsedTradeService;
import com.onebucket.global.auth.jwtAuth.service.RefreshTokenServiceImpl;
import com.onebucket.global.exceptionManage.customException.boardManageException.UserBoardException;
import com.onebucket.global.exceptionManage.errorCode.BoardErrorCode;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseWithIdDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * <br>package name   : com.onebucket.domain.boardManage.api
 * <br>file name      : UsedTradePostController
 * <br>date           : 2024-11-19
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
@RequestMapping("/used-post")
public class UsedTradePostController extends AbstractPostController<UsedTradePostService> {
    private final UsedTradeService usedTradeService;

    public UsedTradePostController(UsedTradePostService postService,
                                   SecurityUtils securityUtils,
                                   MemberService memberService,
                                   BoardService boardService,
                                   UsedTradeService usedTradeService,
                                   ChatRoomService chatRoomService, RefreshTokenServiceImpl refreshTokenServiceImpl) {
        super(postService, securityUtils, memberService, boardService);
        this.usedTradeService = usedTradeService;
    }

    @PreAuthorize("@authorizationService.isUserCanAccessBoard(#dto.post.boardId)")
    @PostMapping("/create")
    public ResponseEntity<SuccessResponseWithIdDto> createPost(@RequestBody UsedTradePostDto.RequestCreate dto) {
        PostDto.Create postCreateDto = dto.getPost();
        UsedTradeDto.Create tradeCreateDto = dto.getTrade();

        String type = boardService.getType(postCreateDto.getBoardId());
        if(!type.equals("usedTradePost")) {
            throw new UserBoardException(BoardErrorCode.MISMATCH_POST_AND_BOARD);
        }

        Long ownerId = securityUtils.getUserId();
        tradeCreateDto.setOwnerId(ownerId);

        Long tradeId = usedTradeService.createTrade(tradeCreateDto);

        UsedTradePostDto.Create usedTradePostCreateDto = UsedTradePostDto.Create.builder()
                .boardId(postCreateDto.getBoardId())
                .title(postCreateDto.getTitle())
                .text(postCreateDto.getText())
                .tradeId(tradeId)
                .userId(ownerId)
                .build();

        Long postId = postService.createPost(usedTradePostCreateDto);

        return ResponseEntity.ok(new SuccessResponseWithIdDto("success create used trade post", postId));
    }

    @PreAuthorize("@authorizationService.isUserOwnerOfPost(#dto.post.postId)")
    @PostMapping("/update")
    public ResponseEntity<SuccessResponseWithIdDto> updatePost(@RequestBody UsedTradePostDto.RequestUpdate dto) {
        PostDto.Update updatePostDto = dto.getPost();
        UsedTradeDto.Update updateTradeDto = dto.getTrade();

        Long tradeId = postService.getTradeId(updatePostDto.getPostId());
        updateTradeDto.setTradeId(tradeId);

        usedTradeService.update(updateTradeDto);

        postService.updatePost(updatePostDto);

        return ResponseEntity.ok(new SuccessResponseWithIdDto("success update post", updatePostDto.getPostId()));
    }

    @PreAuthorize("@authorizationService.isUserOwnerOfPost(#postId)")
    @PostMapping("/lift/{postId}")
    public ResponseEntity<SuccessResponseWithIdDto> liftPost(@PathVariable Long postId) {
        Optional<LocalDateTime> liftTime = postService.liftPost(postId);
        return liftTime.map(localDateTime -> ResponseEntity.ok(new SuccessResponseWithIdDto(localDateTime.toString(), postId)))
                .orElseGet(() -> ResponseEntity.ok(new SuccessResponseWithIdDto("false", -1L)));
    }
    @Override
    protected ResponseEntity<? extends PostDto.ResponseInfo> getPostInternal(PostKeyDto.UserPost dto) {

        UsedTradePostDto.Info info = postService.getPost(dto);

        PostDto.ResponseInfo rawResponseInfo = convertInfoToResponse(info, dto);

        Long tradeId = info.getTradeId();

        UsedTradeDto.Info tradeInfo = usedTradeService.getInfo(tradeId);

        UsedTradePostDto.ResponseInfo responseInfo = UsedTradePostDto.ResponseInfo.of(rawResponseInfo);
        responseInfo.setTrade(tradeInfo);

        return ResponseEntity.ok(responseInfo);
    }

    @Override
    protected ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostsBySearchInternal(PostKeyDto.SearchPage dto) {
        if(!Objects.equals(boardService.getType(dto.getBoardId()), "usedTradePost")) {
            throw new UserBoardException(BoardErrorCode.MISMATCH_POST_AND_BOARD);
        }
        Page<UsedTradePostDto.Thumbnail> posts = postService.getSearchResult(dto)
                .map(this::getThumbnailUsedTradeInternal);

        return ResponseEntity.ok(posts);
    }

    @Override
    protected ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostByBoardInternal(PostKeyDto.BoardPage dto) {
        Page<UsedTradePostDto.Thumbnail> posts = postService.getPostsByBoard(dto)
                .map(this::getThumbnailUsedTradeInternal);

        return ResponseEntity.ok(posts);
    }

    @Override
    protected ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostByAuthorInternal(PostKeyDto.AuthorPage dto) {
        Page<UsedTradePostDto.Thumbnail> posts = postService.getPostByAuthorId(dto)
                .map(this::getThumbnailUsedTradeInternal);

        return ResponseEntity.ok(posts);
    }

    private UsedTradePostDto.Thumbnail getThumbnailUsedTradeInternal(PostDto.InternalThumbnail internalThumbnail) {
        UsedTradeDto.ListedInfo tradeInfo =
                UsedTradeDto.ListedInfo.of(usedTradeService
                        .getInfo(((UsedTradePostDto.InternalThumbnail) internalThumbnail)
                                .getTrade()));

        UsedTradePostDto.Thumbnail thumbnail =
                UsedTradePostDto.Thumbnail.of(convertInternalThumbnailToThumbnail(internalThumbnail));
        thumbnail.setLiftedAt(((UsedTradePostDto.InternalThumbnail) internalThumbnail).getLiftedAt());
        thumbnail.setTrade(tradeInfo);

        return thumbnail;
    }
}
