package com.onebucket.global.auth.jwtAuth.service;

import com.onebucket.global.auth.jwtAuth.domain.RefreshToken;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.RegisterException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.redis.RedisRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    private final String HEADEDLY = "refreshToken:";

    @Override
    public void saveRefreshToken(RefreshToken token) {
        if(!StringUtils.hasText(token.getUsername()) || !StringUtils.hasText(token.getRefreshToken())) {
            throw new RegisterException(AuthenticationErrorCode.INVALID_SUBMIT, "username or refresh token is null");
        }
        redisRepository.save()
                .key(HEADEDLY + token.getUsername())
                .value(token.getRefreshToken())
                .timeout(timeOutMillis)
                .timeUnit(TimeUnit.MILLISECONDS)
                .save();
    }

    @Override
    public RefreshToken getRefreshToken(String username) {
        String token = redisRepository.get(HEADEDLY + username);
        if(token != null) {
            return new RefreshToken(username, token);
        } else {
            throw new RegisterException(AuthenticationErrorCode.NON_EXIST_TOKEN);
        }

    }

    @Override
    public void deleteRefreshToken(String username) {
        redisRepository.delete(username);
    }
}
