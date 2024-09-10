package com.onebucket.global.exceptionManage.customException.memberManageExceptoin;

import com.onebucket.global.exceptionManage.errorCode.AuthenticationErrorCode;
import com.onebucket.global.exceptionManage.errorCode.WalletErrorCode;

/**
 * <br>package name   : com.onebucket.global.exceptionManage.customException.memberManageExceptoin
 * <br>file name      : WalletManageException
 * <br>date           : 2024-09-10
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
 * 2024-09-10        SeungHoon              init create
 * </pre>
 */
public class WalletManageException extends MemberManageException {
    public WalletManageException(WalletErrorCode walletErrorcode) {
        super(walletErrorcode);
    }
    public WalletManageException(WalletErrorCode walletErrorcode, String message) {
        super(walletErrorcode, message);
    }
}
