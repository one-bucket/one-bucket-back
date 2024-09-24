package com.onebucket.domain.tradeManage.dao;

import com.onebucket.domain.tradeManage.entity.PendingTrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dao
 * <br>file name      : PendingTradeRepository
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

@Repository
public interface PendingTradeRepository extends JpaRepository<PendingTrade, Long> {
}
