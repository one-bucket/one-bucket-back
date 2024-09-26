package com.onebucket.domain.tradeManage.api;

import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.tradeManage.dto.internal.UserTradeDto;
import com.onebucket.domain.tradeManage.service.PendingTradeService;
import com.onebucket.domain.tradeManage.service.PendingTradeServiceImpl;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseDto;
import com.onebucket.global.utils.SuccessResponseWithIdDto;
import com.sun.net.httpserver.Authenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.api
 * <br>file name      : TradeController
 * <br>date           : 2024-09-26
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
@RequiredArgsConstructor
@RequestMapping("/trade")
public class TradeController {

    private final PendingTradeService pendingTradeService;
    private final MemberService memberService;
    private final SecurityUtils securityUtils;

    @PostMapping("/join/{tradeId}")
    public ResponseEntity<SuccessResponseWithIdDto> joinTrade(@PathVariable Long tradeId) {
        String username = securityUtils.getCurrentUsername();
        Long userId = memberService.usernameToId(username);

        UserTradeDto userTradeDto = UserTradeDto.builder()
                .tradeId(tradeId)
                .userId(userId)
                .build();

        Long id = pendingTradeService.addMember(userTradeDto);

        return ResponseEntity.ok(new SuccessResponseWithIdDto("success join trade", id));
    }

    @PostMapping("/quit/{tradeId}")
    public ResponseEntity<SuccessResponseDto> quitTrade(@PathVariable Long tradeId) {
        String username = securityUtils.getCurrentUsername();
        Long userId = memberService.usernameToId(username);

        UserTradeDto userTradeDto = UserTradeDto.builder()
                .tradeId(tradeId)
                .userId(userId)
                .build();

        pendingTradeService.quitMember(userTradeDto);

        return ResponseEntity.ok(new SuccessResponseDto("success quit trade"));
    }



}
