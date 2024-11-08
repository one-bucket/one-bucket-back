package com.onebucket.domain.tradeManage.api;

import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.tradeManage.dto.TradeDto;
import com.onebucket.domain.tradeManage.dto.TradeKeyDto;

import com.onebucket.domain.tradeManage.service.PendingTradeService;
import com.onebucket.domain.tradeManage.service.TradeTagService;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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
    private final TradeTagService tradeTagService;
    private final MemberService memberService;
    private final SecurityUtils securityUtils;

    @PostMapping("/join/{tradeId}")
    public ResponseEntity<TradeDto.ResponseJoinTrade> joinTrade(@PathVariable Long tradeId) {
        String username = securityUtils.getCurrentUsername();
        Long userId = memberService.usernameToId(username);

        TradeKeyDto.UserTrade userTradeDto = TradeKeyDto.UserTrade.builder()
                .tradeId(tradeId)
                .userId(userId)
                .build();

        TradeDto.ResponseJoinTrade response = pendingTradeService.addMember(userTradeDto);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/quit/{tradeId}")
    public ResponseEntity<SuccessResponseDto> quitTrade(@PathVariable Long tradeId) {
        String username = securityUtils.getCurrentUsername();
        Long userId = memberService.usernameToId(username);

        TradeKeyDto.UserTrade userTradeDto = TradeKeyDto.UserTrade.builder()
                .tradeId(tradeId)
                .userId(userId)
                .build();

        pendingTradeService.quitMember(userTradeDto);

        return ResponseEntity.ok(new SuccessResponseDto("success quit trade"));
    }

    @PostMapping("/finish")
    public ResponseEntity<SuccessResponseDto> makeFinishTrade(@RequestBody TradeKeyDto.RequestFinish dto) {
        TradeKeyDto.Finish finishDto = TradeKeyDto.Finish.of(dto);

        pendingTradeService.makeFinish(finishDto);

        return ResponseEntity.ok(new SuccessResponseDto("success finish trade"));
    }

    @GetMapping("/tag/list")
    public ResponseEntity<List<String>> getTagList() {
        List<String> tagList = tradeTagService.getTagList();
        return ResponseEntity.ok(tagList);
    }

    @PostMapping("/date/extend")
    @PreAuthorize("@authorizationService.isUserOwnerOfPost(extendDate.tradeId)")
    public ResponseEntity<SuccessResponseDto> extendDueDate(TradeKeyDto.RequestExtendDate extendDate) {
        LocalDateTime time = pendingTradeService.extendDueDate(TradeKeyDto.ExtendDate.of(extendDate));

        return ResponseEntity.ok(new SuccessResponseDto(time.toString()));
    }





}
