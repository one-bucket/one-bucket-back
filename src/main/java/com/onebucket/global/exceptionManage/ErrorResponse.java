package com.onebucket.global.exceptionManage;

import com.onebucket.global.exceptionManage.errorCode.ErrorCode;
import lombok.Getter;

/**
 * <br>package name   : com.onebucket.global.exceptionManage
 * <br>file name      : ErrorResponse
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
public class ErrorResponse {
    private final String code;
    private final String type;
    private final String message;
    private final String internalMessage;

    public ErrorResponse(ErrorCode errorCode, String message) {
        this.code = errorCode.getCode();
        this.type = errorCode.getType();
        this.message = errorCode.getMessage();
        this.internalMessage = message;
    }
}
