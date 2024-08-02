package com.onebucket.domain.chatManage.service;

import com.onebucket.domain.chatManage.pubsub.RedisSubscriber;
import com.onebucket.global.minio.MinioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import static javax.management.Query.eq;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * <br>package name   : com.onebucket.domain.chatManage.service
 * <br>file name      : ChatRoomServiceImplTest
 * <br>date           : 2024-07-23
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
 * 2024-07-23        SeungHoon              init create
 * </pre>
 */
@ExtendWith(MockitoExtension.class)
class ChatRoomServiceImplTest {
    @Mock
    private RedisMessageListenerContainer redisMessageListener;

    @Mock
    private RedisSubscriber redisSubscriber;

    @Mock
    private MinioRepository minioRepository;

    @InjectMocks
    private ChatRoomServiceImpl chatRoomService;

    @BeforeEach
    void setUp() {
        chatRoomService.init();
    }

    @Test
    void enterChatRoom_shouldAddNewTopic() {
        String roomId = "room1";

        chatRoomService.enterChatRoom(roomId);
        ChannelTopic topic = chatRoomService.getTopic(roomId);
        assertNotNull(topic);
        assertEquals(roomId, topic.getTopic());
    }
}