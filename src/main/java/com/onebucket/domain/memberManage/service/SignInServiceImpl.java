package com.onebucket.domain.memberManage.service;

import com.onebucket.domain.memberManage.dao.MemberRepository;
import com.onebucket.global.auth.jwtAuth.component.JwtProvider;
import com.onebucket.global.auth.jwtAuth.component.JwtParser;
import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
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

    /**
     * @param username id to sign in
     * @param password password to sign in
     * @return jwt, Bearer [access token] [refresh token]
     * @throws org.springframework.security.core.AuthenticationException when username or password incorrect...
     * @tested true
     */
    @Override
    public JwtToken signInByUsernameAndPassword(String username, String password) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return jwtProvider.generateToken(authentication);
        } catch(BadCredentialsException e) {
            throw new AuthenticationException(AuthenticationErrorCode.CREDENTIAL_INVALID);
        } catch(LockedException e) {
            throw new AuthenticationException(AuthenticationErrorCode.LOCK_ACCOUNT);
        } catch(DisabledException e) {
            throw new AuthenticationException(AuthenticationErrorCode.DISABLED_ACCOUNT);
        } catch(AccountExpiredException e) {
            throw new AuthenticationException(AuthenticationErrorCode.EXPIRED_ACCOUNT);
        } catch(CredentialsExpiredException e) {
            throw new AuthenticationException(AuthenticationErrorCode.CREDENTIAL_EXPIRED_ACCOUNT);
        } catch(InternalAuthenticationServiceException e) {
            throw new AuthenticationException(AuthenticationErrorCode.INTERNAL_AUTHENTICATION_ERROR);
        }
    }
}
