package com.onebucket.domain.boardManage.entity.post;

import com.onebucket.domain.tradeManage.entity.UsedTrade;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.boardManage.entity.post
 * <br>file name      : UsedTradePost
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
@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class UsedTradePost extends Post {

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "trade_id")
    private UsedTrade usedTrade;

    @Column(name = "trade_id", insertable = false, updatable = false)
    private Long usedTradeId;

    private LocalDateTime liftedAt;
}
