package com.onebucket.domain.tradeManage.entity;

import com.onebucket.domain.boardManage.entity.Comment;
import com.onebucket.domain.memberManage.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.simpleframework.xml.strategy.Strategy;

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
@Table(name = "pending_trade")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendingTrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "pending_trade_member",
            joinColumns = @JoinColumn(name = "pending_trade_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
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

    private Long wanted;

    private Long joins;

    private LocalDateTime startTradeAt;

    private LocalDateTime finishTradeAt;

    private Long price;

    private Long count;

    private LocalDateTime dueDate;

    private String location;

    private boolean isFin;

}
