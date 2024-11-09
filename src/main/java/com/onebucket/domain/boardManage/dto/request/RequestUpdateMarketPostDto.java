package com.onebucket.domain.boardManage.dto.request;

import com.onebucket.domain.boardManage.dto.parents.MarketPostDto;
import com.onebucket.domain.tradeManage.dto.TradeDto;
import com.onebucket.domain.tradeManage.entity.PendingTrade;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.request
 * <br>file name      : RequestUpdateMarketPostDto
 * <br>date           : 2024-11-09
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
@Getter
@NoArgsConstructor
public class RequestUpdateMarketPostDto {
    @Valid
    MarketPostDto.Update marketPostUpdateDto;

    @Valid
    TradeDto.Update tradeUpdateDto;
}
