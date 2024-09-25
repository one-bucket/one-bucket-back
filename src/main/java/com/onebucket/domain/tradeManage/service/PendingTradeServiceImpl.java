package com.onebucket.domain.tradeManage.service;

import com.onebucket.domain.tradeManage.dao.PendingTradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.service
 * <br>file name      : PendingTradeServiceImpl
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
@Service
@RequiredArgsConstructor
public class PendingTradeServiceImpl {

    private final PendingTradeRepository pendingTradeRepository;

    public void addMember(Long userId, Long tradeId) {

    }
}
