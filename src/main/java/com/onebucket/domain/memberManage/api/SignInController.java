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
 * 1. post - "/sign-in" by {@link SignInRequestDto} / Return {@link JwtToken}
 * </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-06-27        jack8              init create
 * </pre>
 */

@RestController
@RequiredArgsConstructor
public class SignInController {
    private final SignInService signInService;
    private final RefreshTokenService refreshTokenService;
    private final JwtValidator jwtValidator;
    private final JwtProvider jwtProvider;

    @PostMapping(path = "/sign-in")
    public ResponseEntity<JwtToken> signIn (@Valid @RequestBody SignInRequestDto dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();

        JwtToken jwtToken = signInService.signInByUsernameAndPassword(username, password);

        RefreshToken token = new RefreshToken(username, jwtToken.getRefreshToken());
        refreshTokenService.saveRefreshToken(token);

        return ResponseEntity.ok(jwtToken);
    }

    // TODO: make test case.
    @PostMapping("/refresh-token")
    public ResponseEntity<JwtToken> tokenRefresh(HttpServletRequest request,
                                                 @Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        String accessToken = request.getHeader("Authorization");
        String refreshToken = refreshTokenDto.getRefreshToken();

       Authentication authentication = signInService.getAuthenticationAndValidHeader(accessToken);
       String username = authentication.getName();

        if(refreshToken != null &&
                jwtValidator.isTokenValid(refreshToken) &&
                refreshTokenService.isTokenExist(new RefreshToken(username, refreshToken))) {

            JwtToken newToken = jwtProvider.generateToken(authentication);

            refreshTokenService.saveRefreshToken(new RefreshToken(username, newToken.getRefreshToken()));
            return ResponseEntity.ok(newToken);
        }
        throw new AuthenticationException(AuthenticationErrorCode.NON_EXIST_TOKEN, "access token required or invalid.");
    }

}
