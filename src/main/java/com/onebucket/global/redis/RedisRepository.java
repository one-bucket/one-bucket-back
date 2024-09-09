package com.onebucket.global.redis;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

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

    // post 의 view 관리를 위한 sorted set 로직 추가
    public void addToSortedSet(String key, String value, double score) {
        stringRedisTemplate.opsForZSet().add(key, value, score);
    }

    // Sorted Set에서 범위 내의 값 삭제
    public void removeRangeFromSortedSet(String key, long start, long end) {
        stringRedisTemplate.opsForZSet().removeRange(key, start, end);
    }

    // Sorted Set의 크기 확인
    public Long getSortedSetSize(String key) {
        return stringRedisTemplate.opsForZSet().size(key);
    }
    public void setExpire(String key, long timeout) {
        stringRedisTemplate.expire(key, timeout, TimeUnit.HOURS);
    }
    public Long getRank(String key, String value) {
        return stringRedisTemplate.opsForZSet().rank(key, value);
    }
}
