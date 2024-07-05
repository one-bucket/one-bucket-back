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
    // 대학 정보 조회중에서 발생하는 오류의 종류
    NOT_EXIST_UNIVERSITY("2001",HttpStatus.NOT_FOUND,"Can't find University"),
    INVALID_EMAIL("2002", HttpStatus.BAD_REQUEST, "Invalid student email address"),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;


    @Override
    public String getType() {
        return "UNIVERSITY";
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
