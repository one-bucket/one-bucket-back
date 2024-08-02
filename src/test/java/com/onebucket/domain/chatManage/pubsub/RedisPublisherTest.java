package com.onebucket.domain.chatManage.pubsub;

import static org.mockito.Mockito.*;
import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.global.exceptionManage.customException.chatManageException.ChatManageException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import static org.junit.jupiter.api.Assertions.*;

/**
 * <br>package name   : com.onebucket.domain.chatManage.pubsub
 * <br>file name      : RedisPublisherTest
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
class RedisPublisherTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @InjectMocks
    private RedisPublisher redisPublisher;

    @Test
    public void publish_ShouldSendMessage_WhenTopicIsValid() {
        // given
        ChannelTopic topic = new ChannelTopic("testTopic");
        ChatMessage message = new ChatMessage();
        message.setRoomId("roomId");
        message.setMessage("test message");

        // when
        redisPublisher.publish(topic, message);

        // then
        verify(redisTemplate, times(1)).convertAndSend(topic.getTopic(), message);
    }

    @Test
    public void publish_ShouldThrowException_WhenTopicIsNull() {
        // given
        ChannelTopic topic = null;
        ChatMessage message = new ChatMessage();
        message.setRoomId("roomId");
        message.setMessage("test message");

        // when & then
        assertThrows(ChatManageException.class, () -> redisPublisher.publish(null, message));
    }
}