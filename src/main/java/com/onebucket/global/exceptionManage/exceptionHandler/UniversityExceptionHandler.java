package com.onebucket.global.exceptionManage.exceptionHandler;
import com.onebucket.global.exceptionManage.ErrorResponse;
import com.onebucket.global.exceptionManage.customException.memberManageExceptoin.AuthenticationException;
import com.onebucket.global.exceptionManage.customException.universityManageException.UniversityException;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.exceptionHandler
 * <br>file name      : UniversityExceptionHandler
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
@Order(1)
@RestControllerAdvice
public class UniversityExceptionHandler {

    @ExceptionHandler(UniversityException.class)
    public ResponseEntity<ErrorResponse> handleRegisterException(UniversityException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, ex.getErrorCode().getHttpStatus());
    }
}
