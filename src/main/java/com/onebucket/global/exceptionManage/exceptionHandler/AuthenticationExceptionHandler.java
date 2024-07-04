package com.onebucket.global.exceptionManage.exceptionHandler;

import com.onebucket.global.exceptionManage.ErrorResponse;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



/**
 * <br>package name   : com.onebucket.global.exceptionManage.exceptionHandler
 * <br>file name      : AuthenticationExceptionHandler
 * <br>date           : 2024-06-27
 * <br>TODO: when new exception handler add, must add test code..
 * <pre>
 * <span style="color: white;">[description]</span>
 * Exception Handler about Authentication Exception.
 * </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-06-27        jack8              init create
 * </pre>
 * @see AuthenticationException
 */

@Order(1)
@RestControllerAdvice
public class AuthenticationExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleRegisterException(AuthenticationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, ex.getErrorCode().getHttpStatus());
    }
}
