package com.onebucket.global.exceptionManage.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.errorCode
 * <br>file name      : WalletErrorCode
 * <br>date           : 2024-09-09
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
 * 2024-09-09        SeungHoon              init create
 * </pre>
 */
@Getter
@RequiredArgsConstructor
public enum WalletErrorCode implements ErrorCode {
    AMOUNT_CANNOT_BE_NULL("6000",HttpStatus.BAD_REQUEST,"amount can't be null"),
    AMOUNT_MUST_BE_POSITIVE("6001",HttpStatus.BAD_REQUEST,"amount must be positive"),
    WALLET_NOT_FOUND("6002",HttpStatus.NOT_FOUND,"wallet not found"),
    DATA_ACCESS_ERROR("6003",HttpStatus.BAD_REQUEST,"data access error"),
    INSUFFICIENT_BALANCE("6004",HttpStatus.BAD_REQUEST,"insufficient balance"),

    INTERNAL_ERROR("6099",HttpStatus.INTERNAL_SERVER_ERROR,"internal error"),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getType() {
        return "Wallet";
    }

    @Override
    public String getCode() {
        return "";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return null;
    }

    @Override
    public String getMessage() {
        return "";
    }
}
