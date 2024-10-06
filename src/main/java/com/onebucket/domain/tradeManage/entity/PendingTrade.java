package com.onebucket.domain.tradeManage.entity;

import com.onebucket.domain.memberManage.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.tradeManage.entity
 * <br>file name      : pendingHistory
 * <br>date           : 9/24/24
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
//@Table(name = "pending_trade")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PendingTrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = true)
    private Member owner;

    @ManyToMany
    @JoinTable(
            name = "pending_trade_member",
            joinColumns = @JoinColumn(name = "pending_trade_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    @Builder.Default
    private List<Member> members = new ArrayList<>();

    public void addMember(Member member) {
        if (!members.contains(member)) {
            members.add(member);
        }
    }
    public void deleteMember(Member member) {
        members.remove(member);
    }

    private String item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tag_id", nullable = false)
    private TradeTag tradeTag;

    private String linkUrl;

    private Long wanted;

    private Long joins;

    private LocalDateTime startTradeAt;

    private LocalDateTime finishTradeAt;

    private Long price;

    private Long count;

    private LocalDateTime dueDate;

    public void extendDueDate(Long date) {
        if(dueDate != null) {
            this.dueDate = dueDate.plusDays(date);
        }
    }

    private String location;

    private boolean isFin;

}
