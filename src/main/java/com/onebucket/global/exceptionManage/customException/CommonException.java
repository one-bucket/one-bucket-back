package com.onebucket.global.exceptionManage.customException;

import com.onebucket.global.exceptionManage.errorCode.CommonErrorCode;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.customException
 * <br>file name      : CommonException
 * <br>date           : 2024-07-19
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
 * 2024-07-19        jack8              init create
 * </pre>
 */
public class CommonException extends BaseCustomException {
     public CommonException(CommonErrorCode commonErrorCode) {
        super(commonErrorCode);
    }

    public CommonException(CommonErrorCode commonErrorCode, String message) {
        super(commonErrorCode, message);
    }
}
