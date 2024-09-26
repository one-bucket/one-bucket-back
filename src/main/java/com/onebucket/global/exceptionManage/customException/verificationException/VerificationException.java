package com.onebucket.global.exceptionManage.customException.verificationException;

import com.onebucket.global.exceptionManage.customException.BaseCustomException;
import com.onebucket.global.exceptionManage.errorCode.ErrorCode;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.customException.verificationException
 * <br>file name      : VerificationException
 * <br>date           : 2024-09-26
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-09-26        SeungHoon              init create
 * </pre>
 */
public class VerificationException extends BaseCustomException {
    public VerificationException(ErrorCode errorCode) {super(errorCode);}

    public VerificationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
