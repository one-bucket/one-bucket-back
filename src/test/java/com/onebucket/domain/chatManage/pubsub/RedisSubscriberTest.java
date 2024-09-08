package com.onebucket.domain.chatManage.pubsub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.domain.chatManage.dto.chatmessage.ChatMessageDto;
import com.onebucket.global.exceptionManage.customException.chatManageException.ChatManageException;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.connection.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;


/**
 * <br>package name   : com.onebucket.domain.chatManage.pubsub
 * <br>file name      : RedisSubscriberTest
 * <br>date           : 2024-07-09
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
 * 2024-07-09        SeungHoon              init create
 * </pre>
 */
@ExtendWith(MockitoExtension.class)
class RedisSubscriberTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RedisTemplate<String,Object> redisTemplate;

    @Mock
    private SimpMessageSendingOperations messagingTemplate;

    @Mock
    private RedisSerializer<String> redisSerializer;

    @InjectMocks
    private RedisSubscriber redisSubscriber;

    @Test
    @DisplayName("메세지 구독하기 성공")
    public void onMessage_SendMessageToSubscribers() throws Exception {
        // given
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRoomId("roomId");
        chatMessage.setMessage("test message");
        ChatMessageDto messageDto = ChatMessageDto.from(chatMessage);
        String publishMessage = "serialized message";
        byte[] messageBody = publishMessage.getBytes(StandardCharsets.UTF_8);
        Message message = mock(Message.class);

        when(message.getBody()).thenReturn(messageBody);
        when(redisTemplate.getStringSerializer()).thenReturn(redisSerializer);
        when(redisSerializer.deserialize(messageBody)).thenReturn(publishMessage);
        when(objectMapper.readValue(publishMessage, ChatMessageDto.class)).thenReturn(messageDto);

        // when
        redisSubscriber.onMessage(message, null);

        // then
        verify(messagingTemplate, times(1)).convertAndSend("/sub/chat/room/" + messageDto.roomId(), messageDto);
    }

    @Test
    @DisplayName("메세지 구독하기 실패 - 비정상적인 json 데이터")
    public void onMessage_ThrowInvalidJsonFormatException() throws JsonProcessingException {
        // given
        String publishMessage = "invalid json format";
        byte[] messageBody = publishMessage.getBytes(StandardCharsets.UTF_8);
        Message message = mock(Message.class);

        when(message.getBody()).thenReturn(messageBody);
        when(redisTemplate.getStringSerializer()).thenReturn(redisSerializer);
        when(redisSerializer.deserialize(messageBody)).thenReturn(publishMessage);
        when(objectMapper.readValue(publishMessage, ChatMessageDto.class)).thenThrow(new JsonProcessingException("Invalid JSON") {});

        // when & then
        ChatManageException exception = assertThrows(ChatManageException.class, () -> {
            redisSubscriber.onMessage(message, null);
        });

        // 예외가 ChatErrorCode.INVALID_JSON_FORMAT과 일치하는지 확인
        assertEquals(ChatErrorCode.INVALID_JSON_FORMAT, exception.getErrorCode());
    }

    @Test
    @DisplayName("메세지 구독하기 실패 - convertAndSend MessagingException 발생")
    public void onMessage_ThrowMessagingException() throws Exception {
        // given
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRoomId("roomId");
        chatMessage.setMessage("test message");
        ChatMessageDto messageDto = ChatMessageDto.from(chatMessage);
        String publishMessage = "doing messaging error";
        byte[] messageBody = publishMessage.getBytes(StandardCharsets.UTF_8);
        Message message = mock(Message.class);

        when(message.getBody()).thenReturn(messageBody);
        when(redisTemplate.getStringSerializer()).thenReturn(redisSerializer);
        when(redisSerializer.deserialize(messageBody)).thenReturn(publishMessage);
        when(objectMapper.readValue(publishMessage, ChatMessageDto.class)).thenReturn(messageDto);
        doThrow(new MessagingException("test exception")).when(messagingTemplate).convertAndSend(anyString(), any(ChatMessageDto.class));

        // when & then
        ChatManageException exception = assertThrows(ChatManageException.class, () -> {
            redisSubscriber.onMessage(message, null);
        });

        // 예외가 ChatErrorCode.MESSAGING_ERROR와 일치하는지 확인
        assertEquals(ChatErrorCode.MESSAGING_ERROR, exception.getErrorCode());
    }
}