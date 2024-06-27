package com.onebucket.global.auth.jwtAuth.service;

import com.onebucket.global.auth.jwtAuth.dao.RefreshTokenRepository;
import com.onebucket.global.auth.jwtAuth.domain.RefreshToken;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


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

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * @param username key of redis
     * @param refreshToken value of redis
     */
    @Override
    public void saveRefreshToken(String username, String refreshToken) {
        RefreshToken token = new RefreshToken(username, refreshToken);
        refreshTokenRepository.save(token);
    }

    /**
     * @param username key of redis, id of what to search
     * @return RefreshToken
     * @throws NoSuchElementException when cannot find
     */
    @Override
    public RefreshToken getRefreshToken(String username) {
        return refreshTokenRepository.findById(username)
                .orElseThrow(() -> new NoSuchElementException("Refresh token not found"));
    }

    /**
     * @param username key of redis, id of what to delete.
     */
    @Override
    public void deleteRefreshToken(String username) {
        refreshTokenRepository.deleteById(username);
    }
}
