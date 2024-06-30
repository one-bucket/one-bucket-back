package com.onebucket.global.auth.jwtAuth.service;

import com.onebucket.global.auth.jwtAuth.domain.RefreshToken;
import com.onebucket.global.redis.RedisRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


/**
 * <br>package name   : com.onebucket.global.auth.jwtAuth.service
 * <br>file name      : RefreshTokenServiceImpl
 * <br>date           : 2024-06-24
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
 * 2024-06-24        jack8              init create
 * </pre>
 */

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RedisRepository redisRepository;

    @Value("${jwt.expireDate.refreshToken}")
    private long timeOutMillis;


    /**
     * @param username key of redis
     * @param refreshToken value of redis
     */
    @Override
    public void saveRefreshToken(String username, String refreshToken) {
        redisRepository.save()
                .key(username)
                .value(refreshToken)
                .timeout(timeOutMillis)
                .timeUnit(TimeUnit.MILLISECONDS)
                .save();
    }

    @Override
    public RefreshToken getRefreshToken(String username) {
        return null;
    }

    @Override
    public void deleteRefreshToken(String username) {

    }
}
