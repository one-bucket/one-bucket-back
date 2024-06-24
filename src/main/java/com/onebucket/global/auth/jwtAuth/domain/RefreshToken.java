package com.onebucket.global.auth.jwtAuth.domain;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

/**
 * <br>package name   : com.onebucket.gloabal.auth.jwtAuth.domain
 * <br>file name      : RefreshToken
 * <br>date           : 2024-06-24
 * <pre>
 * <span style="color: white;">[description]</span>
 * Entity of refresh token used in redis.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 * RefreshToken refreshToken = new RefreshToken("username", "refreshToken");
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-06-24        jack8              init create
 * </pre>
 */

@RedisHash("refreshToken")
@AllArgsConstructor
public class RefreshToken {
    @Id
    private String username;

    private String refreshToken;


}
