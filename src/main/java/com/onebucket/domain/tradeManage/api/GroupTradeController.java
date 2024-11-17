package com.onebucket.domain.tradeManage.api;

import com.onebucket.domain.chatManager.service.ChatRoomService;
import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.tradeManage.dto.TradeKeyDto;
import com.onebucket.domain.tradeManage.service.GroupTradeService;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


    public GroupTradeController(GroupTradeService tradeService,
                                SecurityUtils securityUtils,
                                MemberService memberService) {
        super(tradeService, securityUtils, memberService);
    }

    @PostMapping("/join")
    public ResponseEntity<TradeKeyDto.ResponseJoinTrade> joinTrade(@RequestParam TradeKeyDto.UserTrade dto) {
        TradeKeyDto.ResponseJoinTrade response = tradeService.addMember(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/quit")
    public ResponseEntity<SuccessResponseDto> quitTrade(@RequestParam TradeKeyDto.UserTrade dto) {
        tradeService.quitMember(dto);
        return ResponseEntity.ok(new SuccessResponseDto("success quit trade"));
    }
}
