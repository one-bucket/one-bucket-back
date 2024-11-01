package com.onebucket.domain.chatManager.dto;

import lombok.*;

import java.util.Date;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {

    public enum MessageType {
        ENTER, TALK, LEAVE, IMAGE
    }
    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
    private Date time;

}
