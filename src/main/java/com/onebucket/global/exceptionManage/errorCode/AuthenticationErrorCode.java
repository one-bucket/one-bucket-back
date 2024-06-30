package com.onebucket.global.exceptionManage.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.errorCode
 * <br>file name      : AuthenticationErrorCode
 * <br>date           : 2024-06-27
 * <pre>
 * <span style="color: white;">[description]</span>
 * Enum class when occur exception while Authentication logic, for example, sign-in, register, authorities or else.
 * </pre>
 * <pre>
 * <span style="color: white;">contain:</span>
 * {@code
 * DUPLICATE_USER("1001", HttpStatus.CONFLICT, "Already Exist value")
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
public enum AuthenticationErrorCode implements ErrorCode{

    //회원가입 과정 중 발생하는 오류의 종류
    DUPLICATE_USER("1001", HttpStatus.CONFLICT, "Already Exist value"),
    NON_EXIST_TOKEN("1002", HttpStatus.UNAUTHORIZED, "Token Not Exist"),
    INVALID_SUBMIT("1003", HttpStatus.BAD_REQUEST, "Form to submit is invalid")

    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;


    @Override
    public String getType() {
        return "AUTH";
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
