package com.onebucket.domain.tradeManage.service;

import com.onebucket.domain.tradeManage.dto.BaseTradeDto;
import com.onebucket.domain.tradeManage.dto.TradeKeyDto;

import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.service
 * <br>file name      : TradeService
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
public interface TradeService {
    <D extends BaseTradeDto.Create> Long createTrade(D dto);

    <D extends BaseTradeDto.Info> D getInfo(Long tradeId);

    <D extends BaseTradeDto.Update> void update(D dto);

    boolean makeFin(TradeKeyDto.Finish dto);

    LocalDateTime extendDueDate(TradeKeyDto.ExtendDate dto);
    Long getOwnerOfTrade(TradeKeyDto.FindTrade dto);
}
