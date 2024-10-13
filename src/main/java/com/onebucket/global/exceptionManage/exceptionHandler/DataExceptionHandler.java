package com.onebucket.global.exceptionManage.exceptionHandler;

import com.onebucket.global.exceptionManage.ErrorResponse;
import com.onebucket.global.exceptionManage.errorCode.ValidateErrorCode;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


/**
 * <br>package name   : com.onebucket.global.exceptionManage.exceptionHandler
 * <br>file name      : DataExceptionHandler
 * <br>date           : 2024-06-27
 * <pre>
 * <span style="color: white;">[description]</span>
 * Exception handler about data validate exception.
 * </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-06-27        jack8              init create
 * </pre>
 * @see MethodArgumentNotValidException
 */

@Order(1)
@RestControllerAdvice
public class DataExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessage.append(error.getField())
                    .append(": ")
                    .append(error.getDefaultMessage())
                    .append(" / ");
        });
        if (!errorMessage.isEmpty()) {
            errorMessage.setLength(errorMessage.length() - 3);
        }
        ErrorResponse errorResponse = new ErrorResponse(ValidateErrorCode.INVALID_DATA, errorMessage.toString());
        return new ResponseEntity<>(errorResponse, ValidateErrorCode.INVALID_DATA.getHttpStatus());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException e) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("file is too big. Check if file size is under 10MB");

    }
}
