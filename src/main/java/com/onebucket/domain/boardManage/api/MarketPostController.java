package com.onebucket.domain.boardManage.api;

import com.onebucket.domain.boardManage.dto.parents.MarketPostDto;
import com.onebucket.domain.boardManage.dto.parents.PostDto;
import com.onebucket.domain.boardManage.dto.parents.ValueDto;
import com.onebucket.domain.boardManage.dto.request.RequestCreateMarketPostDto;
import com.onebucket.domain.boardManage.dto.request.RequestUpdateMarketPostDto;
import com.onebucket.domain.boardManage.service.BoardService;
import com.onebucket.domain.boardManage.service.MarketPostService;
import com.onebucket.domain.chatManager.dto.ChatRoomDto;
import com.onebucket.domain.chatManager.service.ChatRoomService;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.tradeManage.dto.TradeDto;
import com.onebucket.domain.tradeManage.dto.TradeKeyDto;
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

import java.util.List;

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
public class MarketPostController extends AbstractPostController<MarketPostService> {
    private final PendingTradeService pendingTradeService;
    private final ChatRoomService chatRoomService;


    public MarketPostController(MarketPostService postService, SecurityUtils securityUtils,
                                MemberService memberService, BoardService boardService,
                                PendingTradeService pendingTradeService,
                                ChatRoomService chatRoomService){
        super(postService, securityUtils, memberService, boardService);
        this.pendingTradeService = pendingTradeService;
        this.chatRoomService = chatRoomService;
    }

    @PreAuthorize("@authorizationService.isUserCanAccessBoard(#dto.marketPostCreateDto.boardId)")
    @PostMapping("/create")
    public ResponseEntity<MarketPostDto.ResponseCreatePostDto> createPost(@RequestBody @Valid RequestCreateMarketPostDto dto) {

        MarketPostDto.RequestCreate marketPostCreateDto = dto.getMarketPostCreateDto();
        TradeDto.RequestCreate tradeCreateDto = dto.getTradeCreateDto();


        String type = boardService.getType(marketPostCreateDto.getBoardId());
        if(!type.equals("marketPost")) {
            throw new UserBoardException(BoardErrorCode.MISMATCH_POST_AND_BOARD);
        }
        Long ownerId = securityUtils.getUserId();
        Long univId = securityUtils.getUnivId();


        //GroupTrade 저장
        TradeDto.Create internalTradeCreateDto = TradeDto.Create.of(tradeCreateDto, ownerId);
        Long tradeId = pendingTradeService.create(internalTradeCreateDto);


        //MarketPost 저장
        MarketPostDto.Create internalMarketPostCreateDto = MarketPostDto.Create
                .of(marketPostCreateDto, ownerId, univId, tradeId);

        Long savedId = postService.createPost(internalMarketPostCreateDto);

        //채팅방 생성
        String chatRoomName = dto.getChatRoomName();
        ChatRoomDto.CreateRoom createRoomDto = ChatRoomDto.CreateRoom.builder()
                .name(chatRoomName)
                .memberId(ownerId)
                .tradeId(tradeId)
                .build();
        String chatRoomId = chatRoomService.createRoom(createRoomDto);

        //pendingTrade에 저장
        TradeKeyDto.SettingChatRoom settingChatRoom = TradeKeyDto.SettingChatRoom.builder()
                .chatRoomId(chatRoomId)
                .tradeId(tradeId)
                .build();
        pendingTradeService.setChatRoom(settingChatRoom);

        MarketPostDto.ResponseCreatePostDto response = MarketPostDto.ResponseCreatePostDto.builder()
                .postId(savedId)
                .chatRoomId(chatRoomId)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@authorizationService.isUserCanAccessPost(#dto.marketPostUpdateDto.postId)")
    @PostMapping("/update")
    public ResponseEntity<SuccessResponseWithIdDto> updatePost(@RequestBody @Valid RequestUpdateMarketPostDto dto) {

        MarketPostDto.Update marketPostUpdateDto = dto.getMarketPostUpdateDto();
        TradeDto.Update tradeUpdateDto = dto.getTradeUpdateDto();

        //GroupTrade update
        pendingTradeService.update(tradeUpdateDto);

        //MarketPost update
        postService.updatePost(marketPostUpdateDto);

        return ResponseEntity.ok(new SuccessResponseWithIdDto("success update post", marketPostUpdateDto.getPostId()));
    }

    @GetMapping("/list/joins")
    public ResponseEntity<Page<MarketPostDto.Thumbnail>> getJoinsMarketPost(Pageable pageable) {
        String username = securityUtils.getCurrentUsername();
        Long userId = memberService.usernameToId(username);
        List<Long> tradeIds = pendingTradeService.getJoinedTradeExceptOwner(userId);

        return ResponseEntity.ok(postService.getPostByTradeIdList(tradeIds, pageable));

    }

    @Override
    protected ResponseEntity<? extends PostDto.ResponseInfo> getPostInternal(ValueDto.FindPost dto) {

        //MarketPost 가져오기
        ValueDto.GetPost getPost = ValueDto.GetPost.of(dto);
        MarketPostDto.Info marketPostInfoDto = (MarketPostDto.Info) postService.getPost(getPost);

        //일부 필드 재정의 인자 값 설정
        Long savedInRedisLikes = postService.getLikesInRedis(marketPostInfoDto.getPostId());
        Long likes = marketPostInfoDto.getLikes() + savedInRedisLikes;

        boolean isUserAlreadyLikes = postService.isUserLikesPost(dto);

        //trade 정보 가져오기
        Long tradeId = marketPostInfoDto.getTradeId();

        TradeDto.Info tradeInfo = pendingTradeService.getInfo(tradeId);
        TradeDto.ResponseInfo responseTradeInfo = TradeDto.ResponseInfo.of(tradeInfo);

        //response 설정
        MarketPostDto.ResponseInfo response = MarketPostDto.ResponseInfo.of(marketPostInfoDto, responseTradeInfo);
        response.setLikes(likes);
        response.setUserAlreadyLikes(isUserAlreadyLikes);

        increaseViewCountInternal(dto);

        return ResponseEntity.ok(response);
    }

    @Override
    protected ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostByBoardInternal(ValueDto.PageablePost getBoardDto) {
        Page<MarketPostDto.Thumbnail> posts = postService.getPostsByBoard(getBoardDto)
                        .map(post -> (MarketPostDto.Thumbnail) post);

        posts.forEach(this::addInfoOnPost);
        return ResponseEntity.ok(posts);
    }

    @Override
    protected ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostsBySearchInternal(ValueDto.SearchPageablePost dto) {
        Page<MarketPostDto.Thumbnail> posts = postService.getSearchResult(dto)
                        .map(post -> (MarketPostDto.Thumbnail) post);

        posts.forEach(this::addInfoOnPost);
        return ResponseEntity.ok(posts);
    }

    private void addInfoOnPost(MarketPostDto.Thumbnail dto) {
        Long commentCount = (Long) postService.getCommentCount(dto.getPostId());
        dto.setCommentsCount(commentCount);
        dto.setLikes(dto.getLikes() + postService.getLikesInRedis(dto.getPostId()));

        //tradeInfo 설정
        Long tradeId = dto.getTradeId();
        TradeDto.Info tradeInfo = pendingTradeService.getInfo(tradeId);
        TradeDto.ResponseInfo responseTradeInfo = TradeDto.ResponseInfo.of(tradeInfo);
        dto.setTradeInfo(responseTradeInfo);
    }
}
