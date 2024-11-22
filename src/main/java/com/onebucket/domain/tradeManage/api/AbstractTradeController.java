package com.onebucket.domain.tradeManage.api;

import com.onebucket.domain.memberManage.service.MemberService;
import com.onebucket.domain.tradeManage.dto.TradeKeyDto;
import com.onebucket.domain.tradeManage.service.TradeService;
import com.onebucket.global.utils.SecurityUtils;
import com.onebucket.global.utils.SuccessResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.api
 * <br>file name      : AbstractTradeController
 * <br>date           : 2024-11-14
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
@RequiredArgsConstructor
public abstract class AbstractTradeController<S extends TradeService> {
    protected final S tradeService;
    protected final SecurityUtils securityUtils;
    protected final MemberService memberService;


    @PostMapping("/extend-date")
    @PreAuthorize("@authorizationService.isUserOwnerOfTrade(#dto.tradeId)")
    public ResponseEntity<SuccessResponseDto> extendDueDate(@RequestBody TradeKeyDto.RequestExtendDate dto) {

        LocalDateTime time = tradeService.extendDueDate(TradeKeyDto.ExtendDate.of(dto));

        return ResponseEntity.ok(new SuccessResponseDto(time.toString()));
    }

    @PostMapping("/finish")
    @PreAuthorize("@authorizationService.isUserOwnerOfTrade(#dto.tradeId)")
    public ResponseEntity<SuccessResponseDto> makeTradeFinish(@RequestBody TradeKeyDto.RequestFinish dto) {
        TradeKeyDto.Finish finishDto = TradeKeyDto.Finish.of(dto);
        tradeService.makeFin(finishDto);
        return ResponseEntity.ok(new SuccessResponseDto(finishDto.isFin() ? "true" : "false"));
    }




}
