package com.onebucket.domain.tradeManage.dao.pendingTrade;

import com.onebucket.domain.tradeManage.entity.BaseTrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.dao
 * <br>file name      : BaseTradeRepository
 * <br>date           : 11/13/24
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
public interface BaseTradeRepository<T extends BaseTrade> extends JpaRepository<T, Long> {
    @Query("SELECT p.id FROM GroupTrade p " +
            "JOIN p.joiners m " +
            "WHERE m.id = :userId AND p.owner.id <> :userId")
    List<Long> findTradeIdsWhereUserIsParticipant(Long userId);
}
