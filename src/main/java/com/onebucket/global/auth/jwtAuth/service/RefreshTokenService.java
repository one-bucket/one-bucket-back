package com.onebucket.global.auth.jwtAuth.service;

import com.onebucket.global.auth.jwtAuth.domain.RefreshToken;

/**
 * <br>package name   : com.onebucket.gloabal.auth.jwtAuth.service
 * <br>file name      : RefreshTokenService
 * <br>date           : 2024-06-24
 * <pre>
 * <span style="color: white;">[description]</span>
 * Interface of RefreshTokenService. Have CRUD algorithm of refresh token via redis.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *     void saveRefreshToken(String username, String refreshToken);
 *     String getRefreshToken(String username);
 *     void deleteRefreshToken(String username);
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-06-24        jack8              init create
 * </pre>
 */
public interface RefreshTokenService {
    void saveRefreshToken(RefreshToken token);
    RefreshToken getRefreshToken(String username);
    void deleteRefreshToken(String username);

    boolean isTokenExist(RefreshToken refreshToken);
}
