package com.onebucket.domain.tradeManage.api;

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


    public GroupTradeController(GroupTradeService tradeService,
                                SecurityUtils securityUtils,
                                MemberService memberService) {
        super(tradeService, securityUtils, memberService);
    }

    @PostMapping("/join/{tradeId}")
    public ResponseEntity<TradeKeyDto.ResponseJoinTrade> joinTrade(@PathVariable Long tradeId) {
        Long userId = securityUtils.getUserId();

        TradeKeyDto.UserTrade dto = TradeKeyDto.UserTrade.builder()
                .tradeId(tradeId)
                .userId(userId)
                .build();
        TradeKeyDto.ResponseJoinTrade response = tradeService.addMember(dto);
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
