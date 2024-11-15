package com.onebucket.domain.memberManage.api;

import com.onebucket.domain.memberManage.dto.RefreshTokenDto;
import com.onebucket.domain.memberManage.dto.SignInRequestDto;
import com.onebucket.domain.memberManage.service.SignInService;
import com.onebucket.global.auth.jwtAuth.component.JwtProvider;
import com.onebucket.global.auth.jwtAuth.component.JwtValidator;
import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import com.onebucket.global.auth.jwtAuth.domain.RefreshToken;
import com.onebucket.global.auth.jwtAuth.service.RefreshTokenService;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    private final JwtValidator jwtValidator;
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

        RefreshToken token = new RefreshToken(username, jwtToken.getRefreshToken());
        refreshTokenService.saveRefreshToken(token);

        return ResponseEntity.ok(jwtToken);
    }


    /**
     * 토큰 재발급을 위한 엔드포인트이며 refresh token 을 받아 redis 에서 존재 여부 및 토큰
     * 유효성  여부를 판단하여 새로운 토큰을 발급해준다. refresh token의 유효 기간은
     * 한 달이며 그 기간 동안 redis 에 저장된다.
     * @param request 헤더에서 access token 을 직접 가져온다.
     * @param refreshTokenDto refresh token
     * @return 200 code, JwtToken
     * @tested yes
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<JwtToken> tokenRefresh(HttpServletRequest request,
                                                 @Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        String accessToken = request.getHeader("Authorization");
        String refreshToken = refreshTokenDto.getRefreshToken();

        //access 토큰 검증 및 예외 처리 로직
        //access 토큰 존재 여부에 대한 예외(AuthenticationErrorCode.NON_VALID_TOKEN)
        //access 토큰 검증에 대한 예외(AuthenticationErrorCode.NON_VALID_TOKEN) - JwtExpiredToken 제외.
       Authentication authentication = signInService.getAuthenticationAndValidHeader(accessToken);
       String username = authentication.getName();

       RefreshToken token2Validate = new RefreshToken(username, refreshToken);

        if(refreshToken != null &&
                jwtValidator.isTokenValid(refreshToken) &&
                refreshTokenService.isTokenExist(token2Validate)) {

            JwtToken newToken = jwtProvider.generateToken(authentication);
            RefreshToken token2Save = new RefreshToken(username, newToken.getRefreshToken());

            refreshTokenService.saveRefreshToken(token2Save);
            return ResponseEntity.ok(newToken);
        }
        throw new AuthenticationException(AuthenticationErrorCode.NON_VALID_TOKEN, "error to validate refresh token");
    }

}
