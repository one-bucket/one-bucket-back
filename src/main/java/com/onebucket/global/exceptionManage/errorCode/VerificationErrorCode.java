package com.onebucket.global.exceptionManage.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.errorCode
 * <br>file name      : VerificationErrorCode
 * <br>date           : 2024-09-26
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
 * 2024-09-26        SeungHoon              init create
 * </pre>
 */
@Getter
@RequiredArgsConstructor
public enum VerificationErrorCode implements ErrorCode {
    DUPLICATE_EMAIL("4000", HttpStatus.CONFLICT, "Duplicate Email"),
    INVALID_EMAIL("4001", HttpStatus.BAD_REQUEST, "Invalid student email address"),
    INVALID_VERIFICATION_CODE("4002", HttpStatus.BAD_REQUEST, "Invalid verification code"),
    VERIFICATION_CODE_NOT_FOUND("4003", HttpStatus.BAD_REQUEST, "Can't find verification code"),

    ;


    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getType() {
        return "Verification";
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
        return message;
    }
}
