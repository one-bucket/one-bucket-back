package com.onebucket.domain.tradeManage.service;

import com.onebucket.domain.tradeManage.dto.internal.UpdatePendingTradeDto;
import com.onebucket.domain.tradeManage.dto.internal.UserTradeDto;
import com.onebucket.domain.tradeManage.dto.request.TradeFinishDto;

import java.time.LocalDateTime;
import java.util.List;

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
    Long addMember(UserTradeDto dto);

    void quitMember(UserTradeDto dto);

    boolean makeFinish(TradeFinishDto.InternalTradeFinishDto dto);

    void update(UpdatePendingTradeDto dto);

    List<String> getMembersNick(Long tradeId);

    void deleteTrade(Long tradeId);

    Long closeTrade(Long tradeId);

    LocalDateTime extendDueDate(Long tradeId);

}
