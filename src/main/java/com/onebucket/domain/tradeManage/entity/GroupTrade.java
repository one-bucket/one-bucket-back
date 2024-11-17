package com.onebucket.domain.tradeManage.entity;

import com.onebucket.domain.chatManager.entity.ChatRoom;
import com.onebucket.domain.memberManage.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
@Getter
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue("GroupTrade")
@Setter
public class GroupTrade extends BaseTrade {

    @ManyToMany
    @JoinTable(
            name = "pending_trade_member",
            joinColumns = @JoinColumn(name = "pending_trade_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    @Builder.Default
    private List<Member> joiners = new ArrayList<>();

    public void addMember(Member member) {
        if (!joiners.contains(member)) {
            joiners.add(member);
            joins++;
        }
    }
    public void deleteMember(Member member) {
        if(joiners.remove(member)) {
            joins--;
        }
    }

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;


    private Long wanted;

    private Long joins;

    private Long count;
}
