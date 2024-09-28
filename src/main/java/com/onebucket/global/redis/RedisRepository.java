package com.onebucket.global.redis;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * <br>package name   : com.onebucket.global.redis
 * <br>file name      : RedisService
 * <br>date           : 2024-06-29
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

@Repository
@RequiredArgsConstructor
public class RedisRepository {
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public void save(String key, String value) {
        stringRedisTemplate.opsForValue().set(key,value);
    }

    public RedisSaveRequestBuilder save() {
        return new RedisSaveRequestBuilder(stringRedisTemplate);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public <T> T get(String key, Class<T> clazz) {
        String value = stringRedisTemplate.opsForValue().get(key);
        if(value != null) {
            try{
                return objectMapper.readValue(value, clazz);
            } catch(JacksonException e) {
                throw new IllegalArgumentException("Can't parse value to object");
            }
        }
        return null;
    }
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    // TODO: test case 작성
    public boolean isTokenExists(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    public Set<String> keys(String pattern) {
        return stringRedisTemplate.keys(pattern);
    }
}
