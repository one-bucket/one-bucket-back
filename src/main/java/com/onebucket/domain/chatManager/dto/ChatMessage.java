package com.onebucket.domain.chatManager.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * <br>package name   : com.onebucket.domain.chatManager.entity
 * <br>file name      : ChatMessage
 * <br>date           : 2024-09-16
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
public class ChatMessage {

    public enum MessageType {
        ENTER, TALK
    }
    private MessageType type;
    private String roomId;
    private String sender;
    private String message;

}
