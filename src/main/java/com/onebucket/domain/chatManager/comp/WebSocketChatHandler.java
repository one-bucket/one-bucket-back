package com.onebucket.domain.chatManager.comp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onebucket.domain.chatManager.dto.ChatMessage;
import com.onebucket.domain.chatManager.dto.ChatRoom;
import com.onebucket.domain.chatManager.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import reactor.core.publisher.Sinks;

/**
 * <br>package name   : com.onebucket.global.webSocket
 * <br>file name      : WebSocketChatHandler
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

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        String payload = textMessage.getPayload();
        System.out.println("In WebSocketChatHandler - payload is " + payload);

        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        System.out.println("chat message is " + chatMessage.getMessage());
        System.out.println("type is " + chatMessage.getType());
        ChatRoom room = chatService.findRoomById(chatMessage.getRoomId());
        room.handlerActions(session, chatMessage, chatService);
    }
}
