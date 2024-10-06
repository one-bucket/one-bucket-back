package com.onebucket.domain.tradeManage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.entity
 * <br>file name      : CloseTrade
 * <br>date           : 2024-09-26
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
public class CloseTrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String item;
    private LocalDateTime startTradeAt;
    private LocalDateTime finishTradeAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<Long> memberIds = new ArrayList<>();
}
