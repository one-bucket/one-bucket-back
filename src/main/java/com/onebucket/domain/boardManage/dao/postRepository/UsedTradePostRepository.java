package com.onebucket.domain.boardManage.dao.postRepository;

import com.onebucket.domain.boardManage.entity.post.UsedTradePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dao.postRepository
 * <br>file name      : UsedTradePostRepository
 * <br>date           : 2024-11-17
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
public interface UsedTradePostRepository extends BasePostRepository<UsedTradePost> {
    @Query("SELECT mp FROM UsedTradePost mp WHERE mp.usedTrade.id IN :tradeIds")
    Page<UsedTradePost> findByTradeIds(List<Long> tradeIds, Pageable pageable);
}
