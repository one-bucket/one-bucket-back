package com.onebucket.domain.chatManager.entity;

import com.onebucket.domain.memberManage.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.chatManager.entity
 * <br>file name      : ChatRoomMember
 * <br>date           : 10/16/24
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
@Getter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ChatRoomMemberId.class)
@Table(
        name = "chat_room_member",
        indexes = {
                @Index(name = "idx_member_id", columnList = "member_id")
        }
)
public class ChatRoomMember {
    @Id
    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @Id
    @ManyToOne
    @JoinColumn(name = "chat_room_id", referencedColumnName = "id")
    private ChatRoom chatRoom;

    private LocalDateTime joinedAt;

    private String role;
}
