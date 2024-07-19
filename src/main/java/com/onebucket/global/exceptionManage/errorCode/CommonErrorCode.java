package com.onebucket.global.exceptionManage.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.errorCode
 * <br>file name      : CommonErrorCode
 * <br>date           : 2024-07-19
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
 * 2024-07-19        jack8              init create
 * </pre>
 */
@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    ILLEGAL_ARGUMENT("9000", HttpStatus.BAD_REQUEST, "invalid data insert"),
    REDIS_CONNECTION_ERROR("9010", HttpStatus.INTERNAL_SERVER_ERROR, "internal server error/connection"),

    DATA_ACCESS_ERROR("9010", HttpStatus.INTERNAL_SERVER_ERROR, "unknown error occur");
    private final String code;
    private final HttpStatus httpStatus;
    private final String message;


    @Override
    public String getType() {
        return "COMMON";
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
