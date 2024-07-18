package com.onebucket.global.exceptionManage.customException.boardManageException;

import com.onebucket.global.exceptionManage.errorCode.ErrorCode;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.customException.boardManageException
 * <br>file name      : UserBoardException
 * <br>date           : 2024-07-18
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
 * 2024-07-18        jack8              init create
 * </pre>
 */
public class UserBoardException extends BoardManageException{
    public UserBoardException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserBoardException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
