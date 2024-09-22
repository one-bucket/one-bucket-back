package com.onebucket.global.redis;

import com.onebucket.domain.chatManage.dto.chatmessage.ChatMessageDto;
import com.onebucket.domain.chatManage.pubsub.RedisSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * <br>package name   : com.onebucket.gloabal.config
 * <br>file name      : RedisConfig
 * <br>date           : 2024-06-24
 * <pre>
 * <span style="color: white;">[description]</span>
 * Configuration class of REDIS, to setting template of redis.
 * Use String,String template, but just use StringRedisTemplate.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 * //First way to save one
 * private final RedisTemplate<String, String> redisTemplate;
 * ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
 * valueOperations.set(id.toString(), token);
 *
 * //Second way to save list
 * private final RedisTemplate<String, String> redisTemplate;
 * ListOperations<String, String> listOperations = redisTemplate.opsForList();
 * listOperations.leftPush(id.toString(), token);
 *
 * //Third way to save simple without directly inject redisTemplate
 * @Resources(name="redisTemplate")
 * private ValueOperations<String, String> valueOperations;
 * //and so on....
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-06-24        jack8              init create
 * </pre>
 */
@Configuration
@EnableRedisRepositories
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(
                new RedisStandaloneConfiguration(host, port)
        );
    }

    /**
     * 어플리케이션에서 사용할 redisTemplate 설정
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic("chatroom");
    }
    /**
     * redis pub/sub 메세지를 처리하는 listener 설정
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter,
            ChannelTopic channelTopic) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, channelTopic);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapterChatRoomList(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "onMessage");
    }
}
