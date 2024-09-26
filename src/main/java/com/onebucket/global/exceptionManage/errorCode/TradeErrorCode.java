package com.onebucket.global.exceptionManage.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.errorCode
 * <br>file name      : TradeErrorCode
 * <br>date           : 9/25/24
 * <pre>
 * <span style="color: white;">[description]</span>
 *
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *
 * } </pre>
 */
@Getter
@RequiredArgsConstructor
public enum TradeErrorCode implements ErrorCode {
    UNKNOWN_TRADE("6001", HttpStatus.BAD_REQUEST, "unknown trade"),
    FULL_TRADE("6002", HttpStatus.CONFLICT, "already full trade member"),
    ALREADY_JOIN("6003", HttpStatus.BAD_REQUEST, "you already join this trade"),
    FINISH_TRADE("6004", HttpStatus.BAD_REQUEST, "already finished trade"),
    DUE_DATE_OVER("6005", HttpStatus.BAD_REQUEST, "due date of this trade is finish"),
    NOT_OWNER_OF_TRADE("6006", HttpStatus.FORBIDDEN, "you are not allowed to access this trade");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getType() {
        return "TRADE";
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
