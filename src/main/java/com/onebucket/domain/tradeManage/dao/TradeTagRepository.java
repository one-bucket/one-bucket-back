package com.onebucket.domain.tradeManage.dao;

import com.onebucket.domain.tradeManage.entity.TradeTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.dao
 * <br>file name      : TradeTagRepository
 * <br>date           : 2024-09-29
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
public interface TradeTagRepository extends JpaRepository<TradeTag, String> {
}
