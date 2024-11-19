package com.onebucket.domain.boardManage.api;

import com.onebucket.domain.boardManage.dto.postDto.GroupTradePostDto;
import com.onebucket.domain.boardManage.dto.postDto.PostDto;
import com.onebucket.domain.boardManage.dto.postDto.PostKeyDto;
import com.onebucket.domain.boardManage.service.BoardService;
import com.onebucket.domain.boardManage.service.postService.GroupTradePostService;
import com.onebucket.domain.chatManager.dto.ChatRoomDto;
import com.onebucket.domain.chatManager.entity.TradeType;
import com.onebucket.domain.chatManager.service.ChatRoomService;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.tradeManage.dto.GroupTradeDto;
import com.onebucket.domain.tradeManage.dto.TradeKeyDto;
import com.onebucket.domain.tradeManage.service.GroupTradeService;
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
import java.util.Objects;

/**
 * <br>package name   : com.onebucket.domain.boardManage.api
 * <br>file name      : GroupTradePostController
 * <br>date           : 2024-11-17
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
@RequestMapping("/group-post")
public class GroupTradePostController extends AbstractPostController<GroupTradePostService> {
    private final GroupTradeService groupTradeService;

    private final ChatRoomService chatRoomService;

    public GroupTradePostController(GroupTradePostService postService,
                                    SecurityUtils securityUtils,
                                    MemberService memberService,
                                    BoardService boardService,
                                    GroupTradeService groupTradeService,
                                    ChatRoomService chatRoomService) {
        super(postService, securityUtils, memberService, boardService);
        this.groupTradeService = groupTradeService;
        this.chatRoomService = chatRoomService;
    }


    @PreAuthorize("@authorizationService.isUserCanAccessBoard(#dto.post.boardId)")
    @PostMapping("/create")
    public ResponseEntity<GroupTradePostDto.ResponseCreate> createPost(@RequestBody GroupTradePostDto.RequestCreate dto) {

        PostDto.Create postCreateDto = dto.getPost();
        GroupTradeDto.Create tradeCreateDto = dto.getTrade();
        String chatRoomName = dto.getChatRoomName();

        String type = boardService.getType(postCreateDto.getBoardId());
        if(!type.equals("groupTradePost")) {
            throw new UserBoardException(BoardErrorCode.MISMATCH_POST_AND_BOARD);
        }

        Long ownerId = securityUtils.getUserId();
        tradeCreateDto.setOwnerId(ownerId);

        //GroupTrade 저장
        Long tradeId = groupTradeService.createTrade(tradeCreateDto);

        GroupTradePostDto.Create groupTradePostCreateDto = GroupTradePostDto.Create.builder()
                .boardId(postCreateDto.getBoardId())
                .title(postCreateDto.getTitle())
                .text(postCreateDto.getText())
                .tradeId(tradeId)
                .userId(ownerId)
                .build();

        Long postId = postService.createPost(groupTradePostCreateDto);

        //채팅방 생성
        ChatRoomDto.CreateRoom createRoomDto = ChatRoomDto.CreateRoom.builder()
                .name(chatRoomName)
                .ownerId(ownerId)
                .tradeType(TradeType.GROUP)
                .tradeId(tradeId)
                .build();

        String chatRoomId = chatRoomService.createRoom(createRoomDto);

        //pendingTrade에 저장
        TradeKeyDto.SettingChatRoom settingChatRoom = TradeKeyDto.SettingChatRoom.builder()
                .chatRoomId(chatRoomId)
                .tradeId(tradeId)
                .build();
        groupTradeService.setChatRoom(settingChatRoom);

        GroupTradePostDto.ResponseCreate response = GroupTradePostDto.ResponseCreate.builder()
                .postId(postId)
                .chatRoomId(chatRoomId)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@authorizationService.isUserCanAccessPost(#dto.post.postId)")
    @PostMapping("/update")
    public ResponseEntity<SuccessResponseWithIdDto> updatePost(@RequestBody @Valid GroupTradePostDto.RequestUpdate dto) {

        PostDto.Update updatePostDto = dto.getPost();
        GroupTradeDto.Update updateTradeDto = dto.getTrade();

        Long tradeId = postService.getTradeId(updatePostDto.getPostId());
        updateTradeDto.setTradeId(tradeId);

        //GroupTrade update
        groupTradeService.update(updateTradeDto);

        //MarketPost update
        postService.updatePost(updatePostDto);

        return ResponseEntity.ok(new SuccessResponseWithIdDto("success update post", updatePostDto.getPostId()));
    }

    @GetMapping("/list/joins")
    public ResponseEntity<Page<GroupTradePostDto.Thumbnail>> getJoinsTrade(Pageable pageable) {
        Long userId = securityUtils.getUserId();
        List<Long> tradeIds = groupTradeService.getJoinedTrade(userId);

        GroupTradePostDto.TradeIdsPageDto tradeIdsDto = GroupTradePostDto.TradeIdsPageDto.builder()
                .pageable(pageable)
                .tradeIds(tradeIds)
                .build();

        Page<GroupTradePostDto.Thumbnail> response = postService.getPostByTradeIdList(tradeIdsDto)
                                                             .map(this::getThumbnailGroupTradeInternal);

        return ResponseEntity.ok(response);

    }

    @Override
    protected ResponseEntity<? extends PostDto.ResponseInfo> getPostInternal(PostKeyDto.UserPost dto) {

        GroupTradePostDto.Info info = postService.getPost(dto);

        //like 및 comment 담고, responseInfo로 반환
        PostDto.ResponseInfo rawResponseInfo = convertInfoToResponse(info, dto);

        Long tradeId = info.getTradeId();

        //trade 정보 가져오기 및 타입 캐스팅
        GroupTradeDto.Info tradeInfo = groupTradeService.getInfo(tradeId);


        //groupTradePost로  타입캐스팅 및 trade 절보 삽입
        GroupTradePostDto.ResponseInfo response = GroupTradePostDto.ResponseInfo.of(rawResponseInfo);
        response.setTrade(tradeInfo);
        return ResponseEntity.ok(response);
    }


    @Override
    protected ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostsBySearchInternal(PostKeyDto.SearchPage dto) {

        if(!Objects.equals(boardService.getType(dto.getBoardId()), "groupTradePost")) {
            throw new UserBoardException(BoardErrorCode.MISMATCH_POST_AND_BOARD);
        }
        Page<GroupTradePostDto.Thumbnail> posts = postService.getSearchResult(dto)
                .map(this::getThumbnailGroupTradeInternal);

        return ResponseEntity.ok(posts);
    }

    @Override
    protected ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostByBoardInternal(PostKeyDto.BoardPage dto) {
        Page<GroupTradePostDto.Thumbnail> posts = postService.getPostsByBoard(dto)
                .map(this::getThumbnailGroupTradeInternal);

        return ResponseEntity.ok(posts);
    }

    @Override
    protected ResponseEntity<Page<? extends PostDto.Thumbnail>> getPostByAuthorInternal(PostKeyDto.AuthorPage dto) {
        Page<GroupTradePostDto.Thumbnail> posts = postService.getPostByAuthorId(dto)
                .map(this::getThumbnailGroupTradeInternal);

        return ResponseEntity.ok(posts);
    }

    private GroupTradePostDto.Thumbnail getThumbnailGroupTradeInternal(PostDto.InternalThumbnail internalThumbnail) {
        GroupTradeDto.ListedInfo tradeInfo =
                GroupTradeDto.ListedInfo.of(groupTradeService
                        .getInfo(((GroupTradePostDto.InternalThumbnail) internalThumbnail)
                        .getTrade()));

        GroupTradePostDto.Thumbnail thumbnail =
                GroupTradePostDto.Thumbnail.of(convertInternalThumbnailToThumbnail(internalThumbnail));
        thumbnail.setLiftedAt(((GroupTradePostDto.InternalThumbnail) internalThumbnail).getLiftedAt());
        thumbnail.setTrade(tradeInfo);

        return thumbnail;
    }

}
