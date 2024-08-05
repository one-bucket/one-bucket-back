package com.onebucket.domain.chatManage.controller;

import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.domain.chatManage.pubsub.RedisPublisher;
import com.onebucket.domain.chatManage.service.ChatMessageService;
import com.onebucket.domain.chatManage.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * <br>package name   : com.onebucket.domain.chatManage.controller
 * <br>file name      : ChatMessageController
 * <br>date           : 2024-07-09
 * <pre>
 * <span style="color: white;">[description]</span>
 * "@MessageMapping("/chat/message")" :  Spring에서 WebSocket 메시지 매핑을 위해 사용.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *  public void chatMessage(ChatMessage chatMessage)
 *  public List<ChatMessage> getChatMessages(@PathVariable("roomId") String roomId)
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-07-09        SeungHoon              init create
 * </pre>
 */
@RequiredArgsConstructor
@RestController
@Slf4j
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    /**
     * 특정 채팅방의 모든 메시지를 조회한다.
     */
    @GetMapping("/chat/messages/{roomId}")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable String roomId) {
        List<ChatMessage> response = chatMessageService.getChatMessages(roomId);
        return ResponseEntity.ok(response);
    }
}
