package com.onebucket.domain.tradeManage.service;

import com.onebucket.domain.tradeManage.dto.TradeKeyDto;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.service
 * <br>file name      : UsedTradeService
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
public interface UsedTradeService extends TradeService {

    void reserve(TradeKeyDto.UserTrade dto);
    void rejectReserve(TradeKeyDto.FindTrade dto);
    boolean isReserved(TradeKeyDto.FindTrade dto);
}
