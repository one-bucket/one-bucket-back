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


    DUPLICATE_BOARD_TYPE("2100", HttpStatus.CONFLICT, "duplicate board type name"),
    DUPLICATE_BOARD("2101", HttpStatus.CONFLICT, "duplicate board"),
    UNKNOWN_BOARD("2102", HttpStatus.BAD_REQUEST, "unknown board"),
    UNKNOWN_BOARD_TYPE("2103", HttpStatus.BAD_REQUEST, "unknown board type");


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
