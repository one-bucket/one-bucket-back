package com.onebucket.domain.boardManage.dao;

import com.onebucket.domain.boardManage.entity.post.GroupTradePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.boardManage.dao
 * <br>file name      : GroupTradePostRepository
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
public interface GroupTradePostRepository extends BasePostRepository<GroupTradePost> {
    @Query("SELECT mp FROM GroupTradePost mp WHERE mp.groupTrade.id IN :pendingTradeIds")
    Page<GroupTradePost> findByPendingTradeIds(List<Long> pendingTradeIds, Pageable pageable);
}
