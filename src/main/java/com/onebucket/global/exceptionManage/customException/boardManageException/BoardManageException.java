package com.onebucket.global.exceptionManage.customException.boardManageException;

import com.onebucket.global.exceptionManage.customException.BaseCustomException;
import com.onebucket.global.exceptionManage.errorCode.ErrorCode;
import lombok.Getter;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.customException.boardManageException
 * <br>file name      : BoardManageException
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
@Getter
public class BoardManageException extends BaseCustomException {
    public BoardManageException(ErrorCode errorCode) {
        super(errorCode);
    }
    public BoardManageException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
