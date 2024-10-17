package com.onebucket.domain.chatManager.api;

import com.onebucket.domain.chatManager.dto.ChatDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

/**
 * <br>package name   : com.onebucket.domain.chatManager.controller
 * <br>file name      : ChatController
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
@RequiredArgsConstructor
@RestController
@Slf4j
public class ChatController {
    private final SimpMessageSendingOperations template;


    @MessageMapping("/enterUser")
    public void enterUser(@Payload ChatDto chat) {
        chat.setMessage(chat.getSender() + "님이 입장하였습니다.");
        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload ChatDto chat) {
        chat.setMessage(chat.getMessage());
        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);
    }
}
