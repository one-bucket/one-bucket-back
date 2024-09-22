package com.onebucket.global.exceptionManage.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.errorCode
 * <br>file name      : GetUniversityErrorCode
 * <br>date           : 2024-07-05
 * <pre>
 * <span style="color: white;">[description]</span>
 * University 관련 에러 모음
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *  NOT_EXIST_UNIVERSITY("2001",HttpStatus.NOT_FOUND,"Can't find University"),
 *  INVALID_EMAIL("2002", HttpStatus.BAD_REQUEST, "Invalid student email address"),
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-07-05        SeungHoon              init create
 * </pre>
 */
@Getter
@RequiredArgsConstructor
public enum UniversityErrorCode implements ErrorCode {
    DUPLICATE_UNIVERSITY("3001", HttpStatus.CONFLICT, "Duplicate University"),
    NOT_EXIST_UNIVERSITY("3002",HttpStatus.NOT_FOUND,"Can't find University"),
    INVALID_EMAIL("3003", HttpStatus.BAD_REQUEST, "Invalid student email address"),
    INVALID_VERIFICATION_CODE("3004", HttpStatus.BAD_REQUEST, "Invalid verification code"),
    VERIFICATION_CODE_NOT_FOUND("3005", HttpStatus.BAD_REQUEST, "Can't find verification code"),

    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;


    @Override
    public String getType() {
        return "UNIV";
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
