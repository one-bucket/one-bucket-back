package com.onebucket.domain.tradeManage.service;

import com.onebucket.domain.tradeManage.dao.PendingTradeRepository;
import com.onebucket.domain.tradeManage.entity.PendingTrade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.service
 * <br>file name      : PendingServiceImpl
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
public class PendingServiceImpl {

    private final PendingTradeRepository pendingTradeRepository;

    public List<PendingTrade> getAll() {
        return pendingTradeRepository.findAll();
    }
}
