package com.onebucket.domain.chatManager.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * <br>package name   : com.onebucket.domain.chatManager.entity
 * <br>file name      : ChatRoomMemberId
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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatRoomMemberId implements Serializable {
    private Long member;
    private String chatRoom;

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        ChatRoomMemberId chatRoomMemberId = (ChatRoomMemberId) o;
        return Objects.equals(member, chatRoomMemberId.getMember())
                && Objects.equals(chatRoom, chatRoomMemberId.getChatRoom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, chatRoom);
    }
}
