package com.onebucket.gloabal.auth.jwtAuth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * <br>package name   : com.onebucket.gloabal.auth.jwtAuth.domain
 * <br>file name      : JwtToken
 * <br>date           : 2024-06-20
 * <pre>
 * <span style="color: white;">[description]</span>
 * This class represents a JWT (JSON Web Token) used for authentication purposes. It contains information about the grant type,
 * access token, and refresh token. This class is annotated with Lombok annotations to automatically generate boilerplate code
 * such as constructors, getters, setters, and the builder pattern.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 * JwtToken jwtToken = JwtToken.builder()
 *                             .grantType("Bearer")
 *                             .accessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
 *                             .refreshToken("dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4g...")
 *                             .build();
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-06-20        jack8              init create
 * </pre>
 */

@Builder
@Data
@AllArgsConstructor
public class JwtToken {

    private String grantType;
    private String accessToken;
    private String refreshToken;

}
