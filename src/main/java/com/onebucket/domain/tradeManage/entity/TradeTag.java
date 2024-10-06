package com.onebucket.domain.tradeManage.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.entity
 * <br>file name      : TradeTag
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
@Entity
@Table(name = "trade_tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeTag {

    @Id
    private String name;
}
