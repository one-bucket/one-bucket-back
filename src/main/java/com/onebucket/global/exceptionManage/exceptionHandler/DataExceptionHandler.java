package com.onebucket.global.exceptionManage.exceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onebucket.global.exceptionManage.ErrorResponse;
import com.onebucket.global.exceptionManage.errorCode.ValidateErrorCode;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


/**
 * <br>package name   : com.onebucket.global.exceptionManage.exceptionHandler
 * <br>file name      : DataExceptionHandler
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

@Order(1)
@RestControllerAdvice
public class DataExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        String errorMessage;
        try {
            errorMessage = objectMapper.writeValueAsString(errors);
        } catch (Exception e) {
            errorMessage = "Failed to parse error messages";
        }
        ErrorResponse errorResponse = new ErrorResponse(ValidateErrorCode.INVALID_DATA, errorMessage);
        return new ResponseEntity<>(errorResponse, ValidateErrorCode.INVALID_DATA.getHttpStatus());
    }
}
