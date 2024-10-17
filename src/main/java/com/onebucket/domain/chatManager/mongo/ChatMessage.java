package com.onebucket.domain.chatManager.mongo;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * <br>package name   : com.onebucket.domain.chatManager.mongo
 * <br>file name      : ChatMessage
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

@Builder
@Getter
public class ChatMessage {
    private String sender;
    private String message;

    @Indexed
    private String timestamp;
}
