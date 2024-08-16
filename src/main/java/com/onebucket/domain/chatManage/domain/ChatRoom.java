package com.onebucket.domain.chatManage.domain;

import com.onebucket.domain.chatManage.dto.ChatMemberDto;
import com.onebucket.global.exceptionManage.customException.chatManageException.Exceptions.ChatRoomFullException;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <br>package name   : com.onebucket.domain.chatManage.domain
 * <br>file name      : ChatRoom
 * <br>date           : 2024-07-08
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-07-08        SeungHoon              init create
 * </pre>
 */
@Getter
@Document(collection = "chatroom")
@NoArgsConstructor
public class ChatRoom  {

    @Id
    private String roomId;
    private String name;

    private LocalDateTime createdAt;
    private String createdBy;

    private Set<ChatMemberDto> members;

    private List<ChatMessage> messages;

    private int maxMembers;

    // 테스트에서만 사용. 추후 로직을 다듬을 수 있다면 삭제하는 거로.
    @Builder
    public ChatRoom(String name, LocalDateTime createdAt, String createdBy, Set<ChatMemberDto> members, int maxMembers) {
        this.name = name;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.members = members;
        this.messages = new ArrayList<>();
        this.maxMembers = maxMembers;
    }

    public void addMember(ChatMemberDto member) {
        members.add(member);
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
    }
}
