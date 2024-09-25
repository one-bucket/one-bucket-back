package com.onebucket.domain.tradeManage.service;


/**
 * <br>package name   : com.onebucket.domain.tradeManage.service
 * <br>file name      : PendingTradeService
 * <br>date           : 2024-09-24
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
    void addMember(Long userId, Long tradeId);
    void quitMember(Long userId, Long tradeId);
    boolean makeFinish(Long tradeId);
    boolean makeUnFinish(Long tradeId);
    boolean extendDeadLine(Long tradeId);
    void updateInfo();
}
