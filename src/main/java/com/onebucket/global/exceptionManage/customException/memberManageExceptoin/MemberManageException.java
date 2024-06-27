package com.onebucket.global.exceptionManage.customException.memberManageExceptoin;

import com.onebucket.global.exceptionManage.customException.BaseCustomException;
import com.onebucket.global.exceptionManage.errorCode.ErrorCode;
import lombok.Getter;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.customException
 * <br>file name      : MemberManageException
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
public class MemberManageException extends BaseCustomException {
    public MemberManageException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MemberManageException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
