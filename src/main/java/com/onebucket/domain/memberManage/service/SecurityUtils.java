package com.onebucket.domain.memberManage.service;

import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.RegisterException;
import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * <br>package name   : com.onebucket.domain.memberManage.service
 * <br>file name      : SecurityUtils
 * <br>date           : 2024-07-01
 * <pre>
 * <span style="color: white;">[description]</span>
 * Security utility class. For get username from SecurityContextHolder, or authorities
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 * String username = securityUtils.getCurrentUsername();
 * } </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-07-01        jack8              init create
 * </pre>
 */
@Component
public class SecurityUtils {

    public String getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication.getName() == null) {
            throw new RegisterException(AuthenticationErrorCode.NON_EXIST_AUTHENTICATION, "can't get authentication from header");
        }
        return authentication.getName();
    }
}
