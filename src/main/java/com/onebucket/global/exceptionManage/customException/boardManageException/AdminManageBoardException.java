package com.onebucket.global.exceptionManage.customException.boardManageException;

import com.onebucket.global.exceptionManage.errorCode.ErrorCode;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.customException.boardManageException
 * <br>file name      : AdminManageBoardException
 * <br>date           : 2024-07-16
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
 * 2024-07-16        jack8              init create
 * </pre>
 */
public class AdminManageBoardException extends BoardManageException {

    public AdminManageBoardException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AdminManageBoardException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
