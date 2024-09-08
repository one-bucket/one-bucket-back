package com.onebucket.domain.chatManage.domain;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.chatManage.domain
 * <br>file name      : ChatMessage
 * <br>date           : 2024-07-08
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *  채팅에는 4가지 종류가 있다.
 *  - ENTER : 채팅방에 처음 입장함. 그 때 안내문을 작성하기 위해 사용한다.
 *  - JOIN : 기존에 이미 채팅방에 입장한 적이 있는 유저가 다시 입장한다.
 *  - TALK : 채팅방에서 유저들이 작성한 채팅을 나타낸다. 이 메세지는 DB에 저장된다.
 *  - Leave : 채팅방에서 유저가 나감 (탈퇴)
 *  본 ChatMessage는 순수히 유저에게 입력받는 채팅 메세지이다.
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
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private MessageType type;
    private String message;

    private String sender;
    private String roomId;

    private String imgUrl;
}
