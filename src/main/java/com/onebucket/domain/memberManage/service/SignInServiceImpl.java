package com.onebucket.domain.memberManage.service;

import com.onebucket.global.auth.jwtAuth.component.JwtProvider;
import com.onebucket.global.auth.jwtAuth.component.JwtValidator;
import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.RegisterException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;


/**
 * <br>package name   : com.onebucket.domain.memberManage.service
 * <br>file name      : SignInServiceImpl
 * <br>date           : 2024-06-27
 * <pre>
 * <span style="color: white;">[description]</span>
 * Implement of {@link SignInService}, use AuthenticationManageBuilder to get Authentication from
 * UsernamePasswordAuthenticationToken.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 * private final SingInService signInService;
 * signInService.signInByUsernameAndPassword(username, password);
 *
 * try{
 *  signInService.signInByUsernameAndPassword(username, password);
 * } catch(AuthenticationException e) {
 *     e.getMessage();
 * }
 * } </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-06-27        jack8              init create
 * 2024-06-30        jack8              [FIX] cause test.
 * </pre>
 */

@Service
@RequiredArgsConstructor
public class SignInServiceImpl implements SignInService{

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;

    /**
     * @param username id to sign in
     * @param password password to sign in
     * @return jwt, Bearer [access token] [refresh token]
     * @throws AuthenticationException when username or password incorrect...
     */
    @Override
    public JwtToken signInByUsernameAndPassword(String username, String password)
            throws AuthenticationException {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        return jwtProvider.generateToken(authentication);
    }

    @Override
    public Authentication getAuthenticationAndValidHeader(String headerString) {

        if (!(headerString != null && headerString.startsWith("Bearer "))) {
            throw new RegisterException(AuthenticationErrorCode.INVALID_SUBMIT, "header form invalid");
        }
        String accessToken = headerString.substring(7);
        try {
            jwtValidator.isTokenValid(accessToken);
        } catch (ExpiredJwtException ignore) {
        } catch (Exception e) {
            throw new RegisterException(AuthenticationErrorCode.INVALID_SUBMIT, "invalid access token");
        }


        return jwtValidator.getAuthentication(accessToken);
    }
}
