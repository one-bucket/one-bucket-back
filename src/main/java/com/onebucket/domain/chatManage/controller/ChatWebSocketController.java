package com.onebucket.domain.chatManage.controller;

import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.domain.chatManage.pubsub.RedisPublisher;
import com.onebucket.domain.chatManage.service.ChatMessageService;
import com.onebucket.domain.chatManage.service.ChatRoomService;
import com.onebucket.global.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
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
 * 2024-08-02        SeungHoon              init create
 * </pre>
 */
@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {
    private final RedisPublisher redisPublisher;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    /**
     * websocket "/pub/chat/message"로 들어오는 메세지를 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(@Payload ChatMessage chatMessage) throws IOException {
        // 처음 입장 했을 경우(Enter)
        if(ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender()+"님이 입장하셨습니다.");
        }
        if(ChatMessage.MessageType.TALK.equals(chatMessage.getType())) {
            chatMessageService.saveMessage(chatMessage);
            chatRoomService.addChatMessage(chatMessage);
        }
        if(ChatMessage.MessageType.LEAVE.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender()+"님이 퇴장하셨습니다.");
        }
        // 기존 유저가 입장하는 경우(Join), 아무것도 출력하지않음.
        // Websocket에 발행된 메세지를 redis로 발행한다.
        ChannelTopic topic = chatRoomService.getTopic(chatMessage.getRoomId());
        redisPublisher.publish(topic, chatMessage);
    }
}
