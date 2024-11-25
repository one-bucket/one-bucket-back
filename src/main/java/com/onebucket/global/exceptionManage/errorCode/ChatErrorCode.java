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

    NOT_EXIST_ROOM("3000",HttpStatus.NOT_FOUND,"Can't find ChatRoom"),
    MESSAGING_ERROR("3001", HttpStatus.INTERNAL_SERVER_ERROR, "Fail to send message"),
    INVALID_JSON_FORMAT("3002", HttpStatus.BAD_REQUEST, "Invalid JSON format"),
    CHAT_IMAGE_ERROR("3003", HttpStatus.INTERNAL_SERVER_ERROR, "Save image failed"),
    CHAT_ROOM_FULL("3004", HttpStatus.BAD_REQUEST, "Chat room has reached the maximum number of members"),
    MAX_MEMBERS_EXCEEDED("3005", HttpStatus.BAD_REQUEST, "Max members exceeded"),
    USER_NOT_CREATOR("3006", HttpStatus.BAD_REQUEST, "User is not creator"),
    USER_NOT_IN_ROOM("3007", HttpStatus.BAD_REQUEST, "User is not in room"),

    DEVICE_TOKEN_NULL("3008", HttpStatus.BAD_REQUEST, "already remove or not match with"),


    INTERNAL_ERROR("3099", HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),

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
