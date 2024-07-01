package com.onebucket.global.exceptionManage.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.errorCode
 * <br>file name      : ValidateErrorCode
 * <br>date           : 2024-06-27
 * <pre>
 * <span style="color: white;">[description]</span>
 * Enum class when occur exception while validate value of dto, {@link javax.xml.validation.Validator validator} annotation
 * or else will be use.
 * </pre>
 * <pre>
 * <span style="color: white;">contain:</span>
 * {@code
 * INVALID_DATA("5001", HttpStatus.BAD_REQUEST)
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
@RequiredArgsConstructor
public enum ValidateErrorCode implements ErrorCode {
    INVALID_DATA("5001", HttpStatus.BAD_REQUEST)
    ;

    private final String code;
    private final HttpStatus httpStatus;
    @Override
    public String getType() {
        return "DATA";
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
