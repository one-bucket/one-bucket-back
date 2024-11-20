package com.onebucket.domain.chatManager.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * <br>package name   : com.onebucket.domain.chatManager.entity
 * <br>file name      : MappingMemberAndChatroom
 * <br>date           : 2024-11-20
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
@IdClass(MappingMemberAndChatroomId.class)
public class MappingMemberAndChatroom {

    @Id
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Long member;

    @Id
    @JoinColumn(name = "trade_id", referencedColumnName = "id")
    private Long trade;

    @JoinColumn(name = "chat_room_id", referencedColumnName = "id")
    private String chatroom;

}
