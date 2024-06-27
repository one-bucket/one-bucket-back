package com.onebucket.connectionTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <br>package name   : com.onebucket.connectionTest
 * <br>file name      : RedisConnectTest
 * <br>date           : 2024-06-26
 * <pre>
 * <span style="color: white;">[description]</span>
 * Test to Redis connect.
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
 * 2024-06-26        jack8              init create
 * </pre>
 */
@SpringBootTest
public class RedisConnectTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void testRedisConnection() {
        //given
        String key = "testKey";
        String value = "testValue";

        //when
        redisTemplate.opsForValue().set(key, value);
        String retrievedValue = redisTemplate.opsForValue().get(key);

        //then
        assertEquals(value, retrievedValue);
    }
}
