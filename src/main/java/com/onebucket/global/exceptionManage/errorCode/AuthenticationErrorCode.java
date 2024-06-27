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
@RequiredArgsConstructor
public enum AuthenticationErrorCode implements ErrorCode{

    //회원가입 과정 중 발생하는 오류의 종류
    DUPLICATE_USER("1001", HttpStatus.CONFLICT, "Already Exist value")

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
