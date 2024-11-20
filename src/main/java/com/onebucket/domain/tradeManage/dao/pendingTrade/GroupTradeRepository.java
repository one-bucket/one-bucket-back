package com.onebucket.domain.tradeManage.dao.pendingTrade;

import com.onebucket.domain.tradeManage.entity.GroupTrade;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dao
 * <br>file name      : GroupTradeRepository
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
public interface GroupTradeRepository extends TradeRepository<GroupTrade> {

    @Query("SELECT gt.id FROM GroupTrade gt JOIN gt.joiners m WHERE m.id = :memberId")
    List<Long> findTradeIdsByMemberId(@Param("memberId") Long memberId);
}
