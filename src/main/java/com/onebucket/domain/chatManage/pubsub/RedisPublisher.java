package com.onebucket.domain.chatManage.pubsub;

import com.onebucket.domain.chatManage.domain.ChatMessage;
import com.onebucket.global.exceptionManage.customException.chatManageException.ChatManageException;
import com.onebucket.global.exceptionManage.errorCode.ChatErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

/**
 * <br>package name   : com.onebucket.domain.chatManage.pubsub
 * <br>file name      : RedisPublisher
 * <br>date           : 2024-07-08
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
 * 2024-07-08        SeungHoon              init create
 * </pre>
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, ChatMessage message) {
        if (topic == null) {
            throw new ChatManageException(ChatErrorCode.NOT_EXIST_TOPIC);
        }
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
