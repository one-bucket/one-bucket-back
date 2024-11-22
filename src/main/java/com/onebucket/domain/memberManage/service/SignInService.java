package com.onebucket.domain.memberManage.service;

import com.onebucket.global.auth.jwtAuth.domain.JwtToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * <br>package name   : com.onebucket.domain.memberManage.service
 * <br>file name      : SignInService
 * <br>date           : 2024-06-27
 * <pre>
 * <span style="color: white;">[description]</span>
 * interface of signInServiceImpl.
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
 * </pre>
 */
public interface SignInService {

    JwtToken signInByUsernameAndPassword(String username, String password)
            throws AuthenticationException;
}
