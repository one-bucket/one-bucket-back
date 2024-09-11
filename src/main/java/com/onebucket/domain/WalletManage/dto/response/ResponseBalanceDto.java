package com.onebucket.domain.WalletManage.dto.response;

import java.math.BigDecimal;

/**
 * <br>package name   : com.onebucket.domain.WalletManage.dto.response
 * <br>file name      : ResponseBalanceDto
 * <br>date           : 2024-09-11
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
 * 2024-09-11        SeungHoon              init create
 * </pre>
 */
public record ResponseBalanceDto(
        BigDecimal balance
) {
    public static ResponseBalanceDto of(BigDecimal balance) {
        return new ResponseBalanceDto(balance);
    }
}
