package com.onebucket.domain.chatManager.entity;

import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.tradeManage.entity.PendingTrade;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.chatManager.entity
 * <br>file name      : ChatRoom
 * <br>date           : 2024-10-15
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
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ChatRoom {

    @Id
    private String id;

    private String name;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default // 기본값 설정
    private List<ChatRoomMember> members = new ArrayList<>();

    public void addMember(Member member) {
        ChatRoomMember chatRoomMember = ChatRoomMember.builder()
                .member(member)
                .chatRoom(this)
                .build();

        this.members.add(chatRoomMember);
    }

    public void quitMember(Member member) {
        ChatRoomMember chatRoomMember = ChatRoomMember.builder()
                .member(member)
                .chatRoom(this)
                .build();

        this.members.remove(chatRoomMember);
    }


    @OneToOne(mappedBy = "chatRoom")
    private PendingTrade pendingTrade;

}
