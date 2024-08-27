package com.onebucket.domain.chatManage.pubsub;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.domain.chatManage.dto.ChatMessageDto;
import com.onebucket.global.exceptionManage.customException.chatManageException.ChatManageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @Mock
    private ChannelTopic channelTopic;

    @InjectMocks
    private RedisPublisher redisPublisher;

    @Test
    @DisplayName("Redis Message 발행하기 성공")
    public void publish_ShouldSendMessage() {
        // Given
        ChatMessage chatMessage = new ChatMessage();
        ChatMessageDto message = ChatMessageDto.from(chatMessage); // ChatMessage 클래스 인스턴스 생성
        // When
        redisPublisher.publish(message);
    }
}