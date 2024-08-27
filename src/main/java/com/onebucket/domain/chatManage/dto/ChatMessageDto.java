package com.onebucket.domain.chatManage.dto;

import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.domain.chatManage.domain.MessageType;

import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.chatManage.dto
 * <br>file name      : ChatMessageDto
 * <br>date           : 2024-08-27
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
 * 2024-08-27        SeungHoon              init create
 * </pre>
 */
public record ChatMessageDto (
        MessageType type,
        String message,
        String sender,
        String roomId,
        String imgUrl,
        LocalDateTime createdAt,
        String createdBy
) {
    public static ChatMessageDto from(ChatMessage m) {
        return new ChatMessageDto(m.getType(),m.getMessage(),m.getSender(),m.getRoomId()
                        ,m.getImgUrl(),m.getCreatedAt(),m.getCreatedBy());
    }
}
