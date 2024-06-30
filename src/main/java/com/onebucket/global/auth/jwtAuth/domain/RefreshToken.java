package com.onebucket.global.auth.jwtAuth.domain;


import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
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

@AllArgsConstructor
@Getter
@Setter
public class RefreshToken {
    @Id
    private String username;

    private String refreshToken;
}
