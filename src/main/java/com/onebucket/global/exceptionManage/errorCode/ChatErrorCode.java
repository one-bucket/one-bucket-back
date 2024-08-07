package com.onebucket.global.exceptionManage.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.errorCode
 * <br>file name      : ChatErrorCode
 * <br>date           : 2024-07-08
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
 * 2024-07-08        SeungHoon              init create
 * </pre>
 */
@Getter
@RequiredArgsConstructor
public enum ChatErrorCode implements ErrorCode {

    NOT_EXIST_TOPIC("3001",HttpStatus.NOT_FOUND,"Can't find topic"),
    NOT_EXIST_ROOM("3002",HttpStatus.NOT_FOUND,"Can't find ChatRoom"),
    NOT_EXIST_Directory("3003",HttpStatus.NOT_FOUND,"Can't find directory"),
    MESSAGING_ERROR("3004", HttpStatus.INTERNAL_SERVER_ERROR, "Fail to send message"),
    INVALID_JSON_FORMAT("3005", HttpStatus.BAD_REQUEST, "Invalid JSON format"),
    INTERNAL_ERROR("3006", HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    SAVE_LOG_FAILED("3007", HttpStatus.INTERNAL_SERVER_ERROR, "Save log failed"),
    CHAT_IMAGE_ERROR("3008", HttpStatus.INTERNAL_SERVER_ERROR, "Save image failed"),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getType() {
        return "CHAT";
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
