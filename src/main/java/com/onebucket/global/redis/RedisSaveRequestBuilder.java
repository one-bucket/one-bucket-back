package com.onebucket.global.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <br>package name   : com.onebucket.global.redis
 * <br>file name      : RedisSaveRequestBuilder
 * <br>date           : 2024-06-29
 * <br>TODO: must change exception occur when transfer object to json string.
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
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-06-29        jack8              init create
 * </pre>
 */
public class RedisSaveRequestBuilder {

    private final StringRedisTemplate redisTemplate;
    private String key;
    private String value;
    private long timeout = -1;
    private  TimeUnit timeUnit = TimeUnit.MINUTES;

    private Object object;
    private boolean isObject = false;

    public RedisSaveRequestBuilder(StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = stringRedisTemplate;
    }

    public RedisSaveRequestBuilder key(String key) {
        this.key = key;
        return this;
    }

    public RedisSaveRequestBuilder value(String value) {
        this.value = value;
        return this;
    }

    public RedisSaveRequestBuilder timeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public RedisSaveRequestBuilder timeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }

    public RedisSaveRequestBuilder object(Object object) {
        this.object = object;
        this.isObject = true;
        return this;
    }


    public void save() {
        if(isObject) {
            saveObject();
        } else {
            saveKeyValue();
        }
    }

    private void saveKeyValue() {
        if(timeout == -1) {
            redisTemplate.opsForValue().set(key, value);
        } else {
            redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
        }
    }

    private void saveObject() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(object);
            if(timeout == -1) {
                redisTemplate.opsForValue().set(key, json);
            } else {
                redisTemplate.opsForValue().set(key, json, timeout, timeUnit);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("failed to serialize object to json", e);
        }
    }
}
