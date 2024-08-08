package com.onebucket.domain.chatManage.domain;

import com.onebucket.domain.chatManage.dto.ChatMemberDto;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
@Builder
@Document(collection = "chatroom")
public class ChatRoom  {

    @Id
    private String roomId;
    private String name;

    private LocalDateTime createdAt;
    private String createdBy;

    @Builder.Default
    private Set<ChatMemberDto> members = ConcurrentHashMap.newKeySet();

    @Builder.Default
    private List<ChatMessage> messages = new ArrayList<>();

    public void addMember(ChatMemberDto member) {
        members.add(member);
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
    }
}
