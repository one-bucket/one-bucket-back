package com.onebucket.domain.boardManage.dao;

import com.onebucket.domain.boardManage.entity.post.MarketPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dao
 * <br>file name      : MarketPostRepository
 * <br>date           : 2024-07-12
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-07-12        jack8              init create
 * </pre>
 */
@Repository
public interface MarketPostRepository extends BasePostRepository<MarketPost> {

    @Query("SELECT mp FROM MarketPost mp WHERE mp.pendingTrade.id IN :pendingTradeIds")
    Page<MarketPost> findByPendingTradeIds(List<Long> pendingTradeIds, Pageable pageable);
}
