package com.onebucket.global.exceptionManage.customException;

import com.onebucket.global.exceptionManage.errorCode.ErrorCode;
import lombok.Getter;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.customException
 * <br>file name      : BaseCustomExcpetion
 * <br>date           : 2024-06-27
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
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-06-27        jack8              init create
 * </pre>
 */
@Getter
public class BaseCustomException extends RuntimeException {
    private final ErrorCode errorCode;

    protected BaseCustomException(ErrorCode errorCode) {
        super(null, null, false, false);
        this.errorCode = errorCode;
    }

    protected BaseCustomException(ErrorCode errorCode, String message) {
        super(message, null, false, false);
        this.errorCode = errorCode;
    }
}
