package com.onebucket.global.auth.jwtAuth.service;

import com.onebucket.global.auth.jwtAuth.component.JwtParser;
import com.onebucket.global.auth.jwtAuth.domain.RefreshToken;
import com.onebucket.global.exceptionManage.customException.CommonException;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.CommonErrorCode;
import com.onebucket.global.redis.RedisRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
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
    private final JwtParser jwtParser;

    @Value("${jwt.expireDate.refreshToken}")
    private long timeOutMillis;

    private final String HEADEDLY = "refreshToken:";

    @Override
    public void saveRefreshToken(String refreshToken) {
        Long userId = jwtParser.getUserIdFromToken(refreshToken);
        if(userId == null || !StringUtils.hasText(refreshToken)) {
            throw new AuthenticationException(AuthenticationErrorCode.NON_VALID_TOKEN, "userId or refresh token is null");
        }
        try {
            redisRepository.save()
                    .key(HEADEDLY + userId)
                    .value(refreshToken)
                    .timeout(timeOutMillis)
                    .timeUnit(TimeUnit.MILLISECONDS)
                    .save();
        } catch (RedisConnectionFailureException e) {
            throw new CommonException(CommonErrorCode.REDIS_CONNECTION_ERROR, "connection fail while save token in redis");
        } catch (DataAccessException e) {
            throw new CommonException(CommonErrorCode.DATA_ACCESS_ERROR, "while save token in redis");
        } catch (IllegalArgumentException e) {
            throw new CommonException(CommonErrorCode.ILLEGAL_ARGUMENT, "invalid data");
        }
    }

    @Override
    public RefreshToken getRefreshToken(Long id) {
        String token = redisRepository.get(HEADEDLY + id);
        if(token != null) {
            return new RefreshToken(id, token);
        } else {
            throw new AuthenticationException(AuthenticationErrorCode.NON_VALID_TOKEN);
        }
    }

    @Override
    public void deleteRefreshToken(String username) {
        redisRepository.delete(username);
    }


    // TODO: test case 작성
    @Override
    public boolean isTokenExist(RefreshToken refreshToken) {
        Long userId = refreshToken.getId();
        String token = refreshToken.getRefreshToken();

        if(redisRepository.isTokenExists(HEADEDLY + userId)) {
            String savedToken = redisRepository.get(HEADEDLY + userId);
            return savedToken.equals(token);
        }
        return false;
    }

}
