package com.onebucket.domain.memberManage.api;

import com.onebucket.domain.memberManage.dto.RefreshTokenDto;
import com.onebucket.domain.memberManage.dto.SignInRequestDto;
import com.onebucket.domain.memberManage.service.MemberService;
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
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final MemberService memberService;
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
     * @return 200 code, JwtToken
     * @tested yes
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<JwtToken> tokenRefresh(HttpServletRequest request) {
        String headerString = request.getHeader("Authorization");
        String refreshToken = jwtValidator.getRefreshToken(headerString);
        Long userId = jwtValidator.getUserIdFromToken(refreshToken);
        String username = memberService.idToUsername(userId);

        if(!refreshTokenService.isTokenExist(new RefreshToken(username, refreshToken))){
            throw new AuthenticationException(AuthenticationErrorCode.NON_VALID_TOKEN);
        }

        JwtToken jwtToken = jwtProvider.generateToken(userId);

        RefreshToken newToken = new RefreshToken(username,jwtToken.getRefreshToken());
        refreshTokenService.saveRefreshToken(newToken);

        return ResponseEntity.ok(jwtToken);
    }

}
