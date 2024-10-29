package com.onebucket.domain.tradeManage.service;

import com.onebucket.domain.tradeManage.dto.TradeDto;
import com.onebucket.domain.tradeManage.dto.TradeKeyDto;


import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.service
 * <br>file name      : PendingTradeService
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
public interface PendingTradeService {
    Long create(TradeDto.Create dto);

    TradeDto.Info getInfo(Long tradeId);

    void update(TradeDto.Update dto);

    Long addMember(TradeKeyDto.UserTrade dto);

    void quitMember(TradeKeyDto.UserTrade dto);

    boolean makeFinish(TradeKeyDto.Finish dto);

    Long terminate(TradeKeyDto.FindTrade dto);

    LocalDateTime extendDueDate(TradeKeyDto.ExtendDate dto);
    void setChatRoom(TradeKeyDto.SettingChatRoom dto);

}
