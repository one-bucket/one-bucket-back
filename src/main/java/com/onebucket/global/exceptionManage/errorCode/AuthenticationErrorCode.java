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
    DUPLICATE_USER("1000", HttpStatus.CONFLICT, "Already Exist user"),
    DUPLICATE_PROFILE("1001", HttpStatus.CONFLICT, "Already Exist profile id"),
    NON_VALID_TOKEN("1002", HttpStatus.UNAUTHORIZED, "Token is null or invalid"),
    INVALID_SUBMIT("1003", HttpStatus.BAD_REQUEST, "Form to submit is invalid"),
    NON_EXIST_AUTHENTICATION("1004",HttpStatus.UNAUTHORIZED, "no authentication or username"),
    NOT_EXIST_AUTHENTICATION_IN_TOKEN("1005", HttpStatus.UNAUTHORIZED, "can't find authentication in token"),
    UNKNOWN_USER("1006", HttpStatus.BAD_REQUEST, "can't find user"),
    UNKNOWN_USER_PROFILE("1007", HttpStatus.NOT_FOUND, "can't find profile"),
    PROFILE_IMAGE_ERROR("1008", HttpStatus.NOT_FOUND, "maybe, can't find image."),

    CREDENTIAL_INVALID("1100", HttpStatus.UNAUTHORIZED, "username or password invalid"),
    LOCK_ACCOUNT("1101", HttpStatus.LOCKED, "account locked"),
    DISABLED_ACCOUNT("1102", HttpStatus.FORBIDDEN, "account disabled"),
    EXPIRED_ACCOUNT("1103", HttpStatus.FORBIDDEN, "account expired"),
    CREDENTIAL_EXPIRED_ACCOUNT("1104", HttpStatus.FORBIDDEN, "account credential expired"),
    INTERNAL_AUTHENTICATION_ERROR("1199", HttpStatus.INTERNAL_SERVER_ERROR, "error while authentication service"),

    UNAUTHORIZED_ACCESS("1200", HttpStatus.FORBIDDEN, "not allowed access")


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
