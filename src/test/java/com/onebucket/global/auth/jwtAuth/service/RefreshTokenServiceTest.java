package com.onebucket.global.auth.jwtAuth.service;

import com.onebucket.global.auth.jwtAuth.component.JwtParser;
import com.onebucket.global.auth.jwtAuth.domain.RefreshToken;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.redis.RedisRepository;
import com.onebucket.global.redis.RedisSaveRequestBuilder;
import com.onebucket.testComponent.mockmember.MockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * <br>package name   : com.onebucket.global.auth.jwtAuth.service
 * <br>file name      : RefreshTokenServiceTest
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
class RefreshTokenServiceTest {

    @Mock
    private RedisRepository redisRepository;

    @Mock
    private RedisSaveRequestBuilder redisSaveRequestBuilder;

    @Mock
    private JwtParser jwtParser;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    @Value("${jwt.expireDate.refreshToken}")
    private long timeOut;

    @Test
    @DisplayName("saveRefreshToken 메서드 성공")
    void testSaveRefreshToken_success() {
        //given
        Long id = -1L;
        String refreshToken = "refresh token";
        //when
        when(redisRepository.save()).thenReturn(redisSaveRequestBuilder);
        when(redisSaveRequestBuilder.key(anyString())).thenReturn(redisSaveRequestBuilder);
        when(redisSaveRequestBuilder.value(anyString())).thenReturn(redisSaveRequestBuilder);
        when(redisSaveRequestBuilder.timeout(anyLong())).thenReturn(redisSaveRequestBuilder);
        when(redisSaveRequestBuilder.timeUnit(TimeUnit.MILLISECONDS)).thenReturn(redisSaveRequestBuilder);
        when(jwtParser.getUserIdFromToken(refreshToken)).thenReturn(id);
        refreshTokenService.saveRefreshToken(refreshToken);

        // then
        verify(redisRepository, times(1)).save();
        verify(redisSaveRequestBuilder).key("refreshToken:" + id);
        verify(redisSaveRequestBuilder).value(refreshToken);
        verify(redisSaveRequestBuilder).timeout(timeOut);
        verify(redisSaveRequestBuilder).timeUnit(TimeUnit.MILLISECONDS);
        verify(redisSaveRequestBuilder).save();
    }


    @Test
    @DisplayName("saveRefreshToken 메서드 실패 - Id 가 없음")
    @MockMember
    void testSaveRefreshToken_fail_nullId() {
        //given
        Long id = null;
        String refreshToken = "valid token";
        when(jwtParser.getUserIdFromToken(refreshToken)).thenReturn(id);
        //when & then
        assertThrows(AuthenticationException.class, () ->
                refreshTokenService.saveRefreshToken(refreshToken));
    }

    @Test
    @DisplayName("saveRefreshToken 메서드 실패 - refreshToken 없음")
    @MockMember
    void testSaveRefreshToken_fail_nullToken() {
        //given
        Long id = -1L;
        String refreshToken = "";
        when(jwtParser.getUserIdFromToken(refreshToken)).thenReturn(id);
        //when & then
        assertThrows(AuthenticationException.class, () ->
                refreshTokenService.saveRefreshToken(refreshToken));
    }


    @Test
    @DisplayName("getRefreshToken 메서드 성공")
    void testGetRefreshToken_success() {
        Long id = -1L;
        String token = "token";

        when(redisRepository.get("refreshToken:" + id)).thenReturn(token);

        RefreshToken result = refreshTokenService.getRefreshToken(id);

        assertEquals(token, result.getRefreshToken());
    }

    @Test
    @DisplayName("getRefreshToken 메서드 실패 - 찾을 수 없는 값")
    void testGetRefreshToken_fail_unknownValue() {
        Long id = -1L;

        when(redisRepository.get(anyString())).thenReturn(null);

        assertThrows(AuthenticationException.class, () ->
                refreshTokenService.getRefreshToken(id));
    }

}