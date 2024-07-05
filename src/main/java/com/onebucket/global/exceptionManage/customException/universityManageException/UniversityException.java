package com.onebucket.global.exceptionManage.customException.universityManageException;

import com.onebucket.global.exceptionManage.errorCode.ErrorCode;
import com.onebucket.global.exceptionManage.errorCode.UniversityErrorCode;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.customException.universityManageException
 * <br>file name      : GetUniversityException
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
public class UniversityException extends UniversityManageException {

    public UniversityException(UniversityErrorCode universityErrorCode) {
        super(universityErrorCode);
    }

    public UniversityException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
