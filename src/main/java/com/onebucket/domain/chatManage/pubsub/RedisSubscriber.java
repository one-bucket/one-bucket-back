package com.onebucket.domain.chatManage.pubsub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.global.exceptionManage.customException.chatManageException.ChatManageException;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

/**
 * <br>package name   : com.onebucket.domain.chatManage.pubsub
 * <br>file name      : RedisSubscriber
 * <br>date           : 2024-07-08
 * <pre>
 * <span style="color: white;">[description]</span>
 * 발행한 메세지는 redisTemplate에 존재하는데, 이를 ChatMessage로 변환하고 구독자들에게 메세지를 전달한다.
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
 * 2024-07-08        SeungHoon              init create
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * redis에서 메세지가 발행되면 대기 하고 있던 onMessage()가 데이터를 처리한다.
     */
    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        try {
            // redis에서 발행된 메세지를 받아 역직렬화
            String publishMessage =  redisTemplate.getStringSerializer().deserialize(message.getBody());
            // ChatMessage 객체로 매핑
            ChatMessage roomMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
            log.info("Redis Subscribe Channel : {}", roomMessage.getRoomId());
            log.info("Redis SUB Message : {}", publishMessage);
            // Websocket 구독자들에게 채팅 메세지 send
            messagingTemplate.convertAndSend("/sub/chat/room/"+roomMessage.getRoomId(),roomMessage);
        } catch (JsonProcessingException e) {
            // JSON 파싱 오류 처리
            log.error("Failed to parse JSON message: {}", e.getMessage());
            throw new ChatManageException(ChatErrorCode.INVALID_JSON_FORMAT);
        } catch (MessagingException e) {
            // convertAndSend 메시징 오류 처리
            log.error("Failed to send message to WebSocket: {}", e.getMessage());
            throw new ChatManageException(ChatErrorCode.MESSAGING_ERROR);
        } catch (Exception e) {
            // 기타 예외 처리
            log.error("Unexpected error occurred: {}", e.getMessage());
            throw new ChatManageException(ChatErrorCode.INTERNAL_ERROR);
        }
    }
}
