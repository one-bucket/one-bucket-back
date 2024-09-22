package com.onebucket.domain.chatManage.dto.chatmessage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.domain.chatManage.domain.MessageType;

import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.chatManage.dto
 * <br>file name      : ChatMessageDto
 * <br>date           : 2024-08-27
 * <pre>
 * <span style="color: white;">[description]</span>
 * 사용자에게 받은 채팅 메세지를 해당 dto로 변환해서 저장한다.
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
public record ChatMessageDto(
        MessageType type,
        String message,
        String sender,
        String roomId,
        String imgUrl,
        @JsonProperty("createdAt")
        @JsonSerialize(using = LocalDateTimeSerializer.class) // 직렬화 시 필요
        @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 역직렬화 시 필요
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt
) {
    public static ChatMessageDto from(ChatMessage m) {
        return new ChatMessageDto(m.getType(),m.getMessage(),m.getSender(),m.getRoomId()
                        ,m.getImgUrl(),LocalDateTime.now());
    }
}
