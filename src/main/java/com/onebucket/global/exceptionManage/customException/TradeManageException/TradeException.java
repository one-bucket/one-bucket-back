package com.onebucket.global.exceptionManage.customException.TradeManageException;

import com.onebucket.global.exceptionManage.customException.BaseCustomException;
import com.onebucket.global.exceptionManage.errorCode.ErrorCode;
import com.onebucket.global.exceptionManage.errorCode.TradeErrorCode;
import lombok.Getter;

import javax.management.JMRuntimeException;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.customException.TradeManageException
 * <br>file name      : TradeException
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
public class TradeException extends BaseCustomException {
    public TradeException(TradeErrorCode errorCode) {
        super(errorCode);
    }
    public TradeException(TradeErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
