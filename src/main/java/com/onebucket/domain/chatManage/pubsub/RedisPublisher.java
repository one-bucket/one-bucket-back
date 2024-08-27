package com.onebucket.domain.chatManage.pubsub;

import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.domain.chatManage.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

/**
 * <br>package name   : com.onebucket.domain.chatManage.pubsub
 * <br>file name      : RedisPublisher
 * <br>date           : 2024-07-08
 * <pre>
 * <span style="color: white;">[description]</span>
 * Redis publisher : 특정 topic으로 메세지를 발행한다.
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
@RequiredArgsConstructor
@Service
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;
    public void publish(ChatMessageDto message) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }
}
