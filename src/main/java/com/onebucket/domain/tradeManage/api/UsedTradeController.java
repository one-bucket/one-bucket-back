package com.onebucket.domain.tradeManage.api;

import com.onebucket.domain.chatManager.dto.ChatRoomDto;
import com.onebucket.domain.chatManager.entity.TradeType;
import com.onebucket.domain.chatManager.service.ChatRoomService;
import com.onebucket.domain.chatManager.service.MappingMemberAndChatroomService;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.tradeManage.dto.TradeKeyDto;
import com.onebucket.domain.tradeManage.dto.UsedTradeDto;
import com.onebucket.domain.tradeManage.service.UsedTradeService;
import com.onebucket.global.exceptionManage.customException.TradeManageException.TradeException;
import com.onebucket.global.exceptionManage.errorCode.TradeErrorCode;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.api
 * <br>file name      : UsedBoardTradeController
 * <br>date           : 11/15/24
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
@RequestMapping("/used-trade")
public class UsedTradeController extends AbstractTradeController<UsedTradeService> {

    private final ChatRoomService chatRoomService;
    private final MappingMemberAndChatroomService mappingMemberAndChatroomService;

    public UsedTradeController(UsedTradeService tradeService,
                               SecurityUtils securityUtils,
                               MemberService memberService,
                               ChatRoomService chatRoomService,
                               MappingMemberAndChatroomService mappingMemberAndChatroomService) {
        super(tradeService, securityUtils, memberService);
        this.chatRoomService = chatRoomService;
        this.mappingMemberAndChatroomService = mappingMemberAndChatroomService;
    }

    @PostMapping("/chat/{tradeId}")
    public ResponseEntity<UsedTradeDto.ResponseCreateChat> createNewChatRoom(@PathVariable Long tradeId) {
        TradeKeyDto.FindTrade findTrade = TradeKeyDto.FindTrade.builder()
                .tradeId(tradeId)
                .build();
        if(tradeService.isReserved(findTrade)) {
            throw new TradeException(TradeErrorCode.ALREADY_JOIN, "already reserved trade");
        }

        Long ownerId = tradeService.getOwnerOfTrade(findTrade);
        Long joinerId = securityUtils.getUserId();
        String ownerNickname = memberService.idToNickname(ownerId);

        ChatRoomDto.SearchMapper searchMapper = ChatRoomDto.SearchMapper.builder()
                .tradeId(tradeId)
                .userId(joinerId)
                .build();

        String roomId = mappingMemberAndChatroomService.getExistChatroom(searchMapper);
        if(roomId != null) {
            UsedTradeDto.ResponseCreateChat response = UsedTradeDto.ResponseCreateChat.builder()
                    .ownerNickname(ownerNickname)
                    .roomId(roomId)
                    .build();

            return ResponseEntity.ok(response);
        }
        ChatRoomDto.CreateAndJoinRoom createRoomDto = ChatRoomDto.CreateAndJoinRoom.builder()
                .name(ownerNickname + "와의 채팅")
                .tradeType(TradeType.USED)
                .tradeId(tradeId)
                .ownerId(ownerId)
                .joinerId(joinerId)
                .build();
        String chatRoomId = chatRoomService.createAndJoinRoom(createRoomDto);

        ChatRoomDto.SaveMapper saveMapper = ChatRoomDto.SaveMapper.builder()
                .tradeId(tradeId)
                .userId(joinerId)
                .roomId(chatRoomId)
                .build();
        mappingMemberAndChatroomService.saveChatRoomMapping(saveMapper);
        UsedTradeDto.ResponseCreateChat response = UsedTradeDto.ResponseCreateChat.builder()
                .roomId(chatRoomId)
                .ownerNickname(ownerNickname)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reserve")
    @PreAuthorize(value = "@authorizationService.isUserOwnerOfTrade(#dto.tradeId)")
    public ResponseEntity<SuccessResponseDto> reserveTradeWithJoiner(@RequestBody TradeKeyDto.UserTrade dto) {
        tradeService.reserve(dto);

        return ResponseEntity.ok(new SuccessResponseDto("Success reserve"));
    }

    //need to refresh
    @PostMapping("/reserve/quit")
    public ResponseEntity<SuccessResponseDto> dismissReserve(@RequestBody TradeKeyDto.FindTrade dto) {
        tradeService.rejectReserve(dto);

        return ResponseEntity.ok(new SuccessResponseDto("Success quit reserve"));
    }




}
