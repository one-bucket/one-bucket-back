package com.onebucket.domain.tradeManage.entity;

import com.onebucket.domain.memberManage.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.entity
 * <br>file name      : BaseTrade
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "trade_type")
public class BaseTrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = true)
    private Member owner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tag_id", nullable = false)
    private TradeTag tradeTag;

    private String item;

    private String linkUrl;

    private Long price;

    private LocalDateTime dueDate;

    private String location;

    private boolean isFin;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    private LocalDate finishAt;

    public void extendDueDate(Long date) {
        if(dueDate != null) {
            this.dueDate = dueDate.plusDays(date);
        }
    }
}
