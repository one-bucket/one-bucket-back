package com.onebucket.global.exceptionManage.errorCode;

import org.springframework.http.HttpStatus;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.errorCode
 * <br>file name      : ErrorCode
 * <br>date           : 2024-06-27
 * <pre>
 * <span style="color: white;">[description]</span>
 * interface of error code.
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
public interface ErrorCode {
    String getType();
    String getCode();

    HttpStatus getHttpStatus();
    String getMessage();
}
