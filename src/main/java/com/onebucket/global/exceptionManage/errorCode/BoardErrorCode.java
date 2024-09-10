package com.onebucket.global.exceptionManage.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.errorCode
 * <br>file name      : BoardErrorCode
 * <br>date           : 2024-07-16
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
 * 2024-07-16        jack8              init create
 * </pre>
 */

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ErrorCode {

    UNKNOWN_POST("2000", HttpStatus.NOT_FOUND, "unknown post "),
    COMMENT_LAYER_OVERHEAD("2001", HttpStatus.BAD_REQUEST, "no re-reply comment"),


    DUPLICATE_BOARD_TYPE("2100", HttpStatus.CONFLICT, "duplicate board type name"),
    DUPLICATE_BOARD("2101", HttpStatus.CONFLICT, "duplicate board"),
    UNKNOWN_BOARD("2102", HttpStatus.BAD_REQUEST, "unknown board"),
    UNKNOWN_COMMENT("2013", HttpStatus.BAD_REQUEST, "unknown comment"),

    UNKNOWN_BOARD_TYPE("2104", HttpStatus.BAD_REQUEST, "unknown board type"),

    I_AM_AN_APPLE_PIE("2999", HttpStatus.INTERNAL_SERVER_ERROR, "something went wrong in server");


    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
    @Override
    public String getType() {
        return "BOARD";
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
