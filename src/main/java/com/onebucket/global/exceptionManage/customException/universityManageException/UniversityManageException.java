package com.onebucket.global.exceptionManage.customException.universityManageException;

import com.onebucket.global.exceptionManage.customException.BaseCustomException;
import com.onebucket.global.exceptionManage.errorCode.ErrorCode;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.customException.universityManageException
 * <br>file name      : UniversityManageException
 * <br>date           : 2024-07-05
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
 * 2024-07-05        SeungHoon              init create
 * </pre>
 */
public class UniversityManageException extends BaseCustomException {
    public UniversityManageException(ErrorCode errorCode) {super(errorCode);}

    public UniversityManageException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
