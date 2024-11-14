package com.onebucket.domain.tradeManage.dao.closedTrade;

import com.onebucket.domain.tradeManage.dao.pendingTrade.BaseTradeRepository;
import com.onebucket.domain.tradeManage.entity.ClosedGroupTrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.dao
 * <br>file name      : ClosedGroupTradeRepository
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
@Repository
public interface ClosedGroupTradeRepository extends BaseTradeRepository<ClosedGroupTrade> {
}
