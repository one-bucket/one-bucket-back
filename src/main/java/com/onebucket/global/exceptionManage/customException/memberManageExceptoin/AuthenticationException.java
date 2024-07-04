package com.onebucket.global.exceptionManage.customException.memberManageExceptoin;

import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.ErrorCode;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.customException.memberManageExceptoin
 * <br>file name      : RegisterException
 * <br>date           : 2024-06-27
 * <pre>
 * <span style="color: white;">[description]</span>
 * Exception use when occur exception while register, for example duplicated or else.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-06-27        jack8              init create
 * </pre>
 */
public class AuthenticationException extends MemberManageException {
    public AuthenticationException(AuthenticationErrorCode authenticationErrorCode) {
        super(authenticationErrorCode);
    }

    public AuthenticationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
