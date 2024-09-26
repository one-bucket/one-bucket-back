package com.onebucket.global.exceptionManage.customException.TradeManageException;

import com.onebucket.global.exceptionManage.errorCode.TradeErrorCode;
import lombok.Getter;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.customException.TradeManageException
 * <br>file name      : PendingTradeException
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
public class PendingTradeException extends TradeException {

    public PendingTradeException(TradeErrorCode errorCode) {
        super(errorCode);
    }
    public PendingTradeException(TradeErrorCode errorCode, String message) {
        super(errorCode, message);
    }


}
