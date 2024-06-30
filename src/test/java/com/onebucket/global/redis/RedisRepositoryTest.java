package com.onebucket.global.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * <br>package name   : com.onebucket.global.redis
 * <br>file name      : RedisServiceTest
 * <br>date           : 2024-06-30
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
 * 2024-06-30        jack8              init create
 * </pre>
 */

@ExtendWith(MockitoExtension.class)
class RedisRepositoryTest {
    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RedisRepository redisRepository;



    @BeforeEach
    void setUp() {
        lenient().when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("save 메서드 성공")
    void testSave_success() {
        String key = "test key";
        String value = "test value";

        redisRepository.save(key, value);
        verify(valueOperations, times(1)).set(key, value);
    }

    @Test
    @DisplayName("get 메서드 성공")
    void testGet_success() {
        String key = "test key";
        String value = "test value";

        when(valueOperations.get(key)).thenReturn(value);

        String result = redisRepository.get(key);

        Assertions.assertEquals(value, result);
    }

    @Test
    @DisplayName("save 메서드 성공 - object에 대해")
    void testSave_success_object() {
        String key = "test Key:";
        String json = "{\"name\":\"test value\"}";
        TestObject testObject = new TestObject("test value");


        redisRepository.save()
                .key(key)
                .object(testObject)
                .save();


        verify(valueOperations, times(1)).set(key, json);
    }

    @Test
    @DisplayName("save 메서드 성공 - time에 대해")
    void testSave_success_time() {
        String key = "test key";
        String value = "test value";
        long timeout = 10L;

        redisRepository.save()
                .key(key)
                .value(value)
                .timeout(timeout)
                .save();

        verify(valueOperations,times(1)).set(key,value, timeout, TimeUnit.MINUTES);
    }

    @Test
    @DisplayName("get 메서드 성공 - 값이 존재하고 변환 가능할 때")
    void testGet_success_object() throws JsonProcessingException {
        String key = "test key";
        String json = "{\"name\":\"test value\"}";
        TestObject testObject = new TestObject("test value");

        when(valueOperations.get(key)).thenReturn(json);
        when(objectMapper.readValue(json, TestObject.class)).thenReturn(testObject);
        TestObject result = redisRepository.get(key, TestObject.class);

        assertNotNull(result);
        assertEquals(testObject, result);
    }

    @Test
    @DisplayName("get 메서드 실패 - 값이 null 인 경우")
    void testGet_fail_nullable() {
        String key = "test key";
        when(valueOperations.get(key)).thenReturn(null);

        TestObject testObject = redisRepository.get(key, TestObject.class);
        assertNull(testObject);
    }

    @Test
    @DisplayName("get 메서드 실패 - json 파싱 오류 실패")
    void testGet_fail_parseError() throws JsonProcessingException {
        String key = "test key";
        String json = "{\"name\":\"test value\"}";

        when(valueOperations.get(key)).thenReturn(json);
        when(objectMapper.readValue(json, TestObject.class)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () ->
                redisRepository.get(key, TestObject.class));
    }

    @Test
    @DisplayName("delete 메서드 성공")
    void testDelete_success() {
        String key = "test key";
        redisRepository.delete(key);
        verify(stringRedisTemplate, times(1)).delete(key);
    }

    @Test
    @DisplayName("delete 메서드 테스트")
    void testDelete() {
        String key = "testKey";

        redisRepository.delete(key);

        verify(stringRedisTemplate, times(1)).delete(key);
    }

    static class TestObject {
        private String name;
        private TestObject(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;
            TestObject that = (TestObject) o;
            return name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

}