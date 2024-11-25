package com.onebucket.domain.tradeManage.api;

import com.onebucket.domain.chatManager.dto.ChatRoomDto;
import com.onebucket.domain.chatManager.service.ChatRoomService;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.tradeManage.dto.TradeKeyDto;
import com.onebucket.domain.tradeManage.service.GroupTradeService;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.api
 * <br>file name      : GroupTradeController
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
@RequestMapping("/group-trade")
public class GroupTradeController extends AbstractTradeController<GroupTradeService> {
    private final ChatRoomService chatRoomService;


    public GroupTradeController(GroupTradeService tradeService,
                                SecurityUtils securityUtils,
                                MemberService memberService,
                                ChatRoomService chatRoomService) {
        super(tradeService, securityUtils, memberService);
        this.chatRoomService = chatRoomService;
    }

    @PostMapping("/join/{tradeId}")
    public ResponseEntity<TradeKeyDto.ResponseJoinTrade> joinTrade(@PathVariable Long tradeId) {
        Long userId = securityUtils.getUserId();

        TradeKeyDto.UserTrade dto = TradeKeyDto.UserTrade.builder()
                .tradeId(tradeId)
                .userId(userId)
                .build();
        TradeKeyDto.ResponseJoinTrade response = tradeService.addMember(dto);

        ChatRoomDto.ManageMember createDto = ChatRoomDto.ManageMember.builder()
                .roomId(response.getChatRoomId())
                .memberId(userId)
                .build();
        chatRoomService.addMember(createDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/quit/{tradeId}")
    public ResponseEntity<SuccessResponseDto> quitTrade(@PathVariable Long tradeId) {
        Long userId = securityUtils.getUserId();

        TradeKeyDto.UserTrade dto = TradeKeyDto.UserTrade.builder()
                .tradeId(tradeId)
                .userId(userId)
                .build();
        tradeService.quitMember(dto);
        return ResponseEntity.ok(new SuccessResponseDto("success quit trade"));
    }
}
