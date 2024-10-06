package com.onebucket.domain.tradeManage.dao;

import com.onebucket.domain.tradeManage.entity.CloseTrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.dao
 * <br>file name      : CloseTradeRepository
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
public interface CloseTradeRepository extends JpaRepository<CloseTrade, Long> {
}
