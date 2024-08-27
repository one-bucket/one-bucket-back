package com.onebucket.domain.chatManage.controller;

import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.domain.chatManage.domain.MessageType;
import com.onebucket.domain.chatManage.dto.chatmessage.ChatMessageDto;
import com.onebucket.domain.chatManage.pubsub.RedisPublisher;
import com.onebucket.domain.chatManage.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.io.IOException;

/**
 * <br>package name   : com.onebucket.domain.chatManage.controller
 * <br>file name      : ChatWebSocketController
 * <br>date           : 2024-08-02
 * <pre>
 * <span style="color: white;">[description]</span>
 * ChatMessage 객체에 roomId을 포함시켜서 MessageMapping 경로를 단순하게 만들었다.
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
 * 2024-08-02        SeungHoon              init create
 * </pre>
 */
@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {
    private final RedisPublisher redisPublisher;
    private final ChatRoomService chatRoomService;

    /**
     * websocket "/pub/chat/message"로 들어오는 메세지를 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(@Payload ChatMessage chatMessage) throws IOException {
        // 메시지 타입에 따라 처리 분기
        switch (chatMessage.getType()) {
            case ENTER:
                handleEnterMessage(chatMessage);
                break;
            case TALK:
                handleTalkMessage(chatMessage);
                break;
            case LEAVE:
                handleLeaveMessage(chatMessage);
                break;
            default:
                // 필요한 경우, 예외 처리나 기본 동작을 추가할 수 있습니다.
                break;
        }
        // 모든 메시지를 공통적으로 처리: Dto 변환 후 Redis에 퍼블리시
        ChatMessageDto chatMessageDto = ChatMessageDto.from(chatMessage);
        redisPublisher.publish(chatMessageDto);
    }

    // 입장 메시지 처리
    private void handleEnterMessage(ChatMessage chatMessage) {
        chatMessage.setMessage(chatMessage.getSender() + "님이 입장하셨습니다.");
        chatRoomService.addChatMembers(chatMessage.getRoomId(), chatMessage.getSender());
    }

    // 대화 메시지 처리
    private void handleTalkMessage(ChatMessage chatMessage) {
        ChatMessageDto chatMessageDto = ChatMessageDto.from(chatMessage);
        chatRoomService.addChatMessages(chatMessageDto);
    }

    // 퇴장 메시지 처리
    private void handleLeaveMessage(ChatMessage chatMessage) {
        chatMessage.setMessage(chatMessage.getSender() + "님이 퇴장하셨습니다.");
        chatRoomService.removeChatMember(chatMessage.getRoomId(), chatMessage.getSender());
    }
}
