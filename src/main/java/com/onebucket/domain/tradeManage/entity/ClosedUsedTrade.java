package com.onebucket.domain.tradeManage.entity;

import com.onebucket.domain.memberManage.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.entity
 * <br>file name      : ClosedUsedTrade
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
@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue("ClosedUsedTrade")
public class ClosedUsedTrade extends BaseTrade {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "joiner_id", nullable = true)
    private Member joiner;
}
