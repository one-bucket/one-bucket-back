package com.onebucket.global.auth.jwtAuth.dao;

import com.onebucket.global.auth.jwtAuth.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * <br>package name   : com.onebucket.gloabal.auth.jwtAuth.dao
 * <br>file name      : RefreshTokenRepository
 * <br>date           : 2024-06-24
 * <pre>
 * <span style="color: white;">[description]</span>
 * RefreshTokenRepository for save refresh token to redis.
 * Extends CrudRepository, implement by interface.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 * private final RefreshTokenRepository refreshTokenRepository;
 *
 * RefreshToken refreshToken = new RefreshToken("username","refreshToken");
 * refreshTokenRepository.save(refreshToken);
 *
 * refreshTokenRepository.findById("username");
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-06-24        jack8              init create
 * </pre>
 */

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}
