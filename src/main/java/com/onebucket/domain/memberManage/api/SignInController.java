package com.onebucket.domain.memberManage.api;

import com.onebucket.domain.memberManage.dto.SignInRequestDto;
import com.onebucket.domain.memberManage.service.SignInService;
import com.onebucket.global.auth.jwtAuth.component.JwtParser;
import com.onebucket.global.auth.jwtAuth.component.JwtProvider;
import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import com.onebucket.global.auth.jwtAuth.service.RefreshTokenService;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * <br>package name   : com.onebucket.domain.memberManage.api
 * <br>file name      : SignInController
 * <br>date           : 2024-06-27
 * <pre>
 * <span style="color: white;">[description]</span>
 * 해당 컨트롤러는 로그인에 대한 endpoint 를 제공한다. 또한 accessToken 이 만료되어 해당 오류 메시지를 반환하는 경우,
 * refreshToken 을 이용한 갱신에 대해 endpoint 를 제공한다.
 * 1. POST - "/sign-in"
 * 2. POST - "/refresh-token"
 * </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-06-27        jack8              init create
 * </pre>
 * @tested yes
 */

@RestController
@RequiredArgsConstructor
public class SignInController {
    private final SignInService signInService;
    private final RefreshTokenService refreshTokenService;
    private final JwtParser jwtParser;
    private final JwtProvider jwtProvider;
    /**
     * 로그인을 위한 엔드포인트. username 과 password 를 이용하여 JwtToken 을 반환하는 컨트롤러이다.
     * @param dto username 과 password
     * @return 200 code, JwtToken
     * @tested yes
     */
    @PostMapping(path = "/sign-in")
    public ResponseEntity<JwtToken> signIn (@Valid @RequestBody SignInRequestDto dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();

        JwtToken jwtToken = signInService.signInByUsernameAndPassword(username, password);

        refreshTokenService.saveRefreshToken(jwtToken.getRefreshToken());

        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtToken> tokenRefresh(@RequestBody JwtToken jwtToken) {

        String accessToken = jwtToken.getAccessToken();
        try {
            jwtParser.isTokenValid(accessToken);
        } catch (ExpiredJwtException ignore) {
        } catch (Exception e) {
            throw new AuthenticationException(AuthenticationErrorCode.NON_VALID_TOKEN,
                    "token form invalid.");
        }

        JwtToken newJwtToken = jwtProvider.generateToken(jwtToken.getRefreshToken());

        refreshTokenService.saveRefreshToken(newJwtToken.getRefreshToken());

        return ResponseEntity.ok(newJwtToken);
    }

}
