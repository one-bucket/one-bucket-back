package com.onebucket.domain.boardManage.dto.request;

import com.onebucket.domain.boardManage.dto.parents.MarketPostDto;
import com.onebucket.domain.tradeManage.dto.TradeDto;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dto.request
 * <br>file name      : RequestCreateMarketPostDto
 * <br>date           : 2024-09-29
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
public class RequestCreateMarketPostDto {


    @Valid
    private MarketPostDto.RequestCreate  marketPostCreateDto;
    @Valid
    private TradeDto.Requestcreate tradeCreateDto;

}
