package com.onebucket.domain.chatManager.mongo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.chatManager.mongo
 * <br>file name      : ChatLog
 * <br>date           : 2024-10-17
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
@Document(collation = "chatLogs")
@Setter
@Getter
public class ChatLog {

    public ChatLog(String roomId) {
        this.roomId = roomId;
    }
    private String roomId;
    private List<ChatMessage> messages;

    public void addMessage(ChatMessage message) {
        this.messages.add(message);
    }
}
