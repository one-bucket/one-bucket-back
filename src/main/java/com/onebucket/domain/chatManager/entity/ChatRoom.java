package com.onebucket.domain.chatManager.entity;

import com.onebucket.domain.tradeManage.entity.PendingTrade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ChatRoom {

    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default // 기본값 설정
    private List<ChatRoomMember> members = new ArrayList<>();

    @OneToOne(mappedBy = "chatRoom")
    private PendingTrade pendingTrade;

}
