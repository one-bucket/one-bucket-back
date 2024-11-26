package com.onebucket.domain.tradeManage.service;

import com.onebucket.domain.tradeManage.dto.TradeKeyDto;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.service
 * <br>file name      : GroupTradeService
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
public interface GroupTradeService extends TradeService {
    TradeKeyDto.ResponseJoinTrade addMember(TradeKeyDto.UserTrade dto);
    void quitMember(TradeKeyDto.UserTrade dto);
    void setChatRoom(TradeKeyDto.SettingChatRoom dto);
    List<Long> getJoinedTrade(Long userId);

    List<Long> getJoinedMemberExceptOwner(Long tradeId);
}
