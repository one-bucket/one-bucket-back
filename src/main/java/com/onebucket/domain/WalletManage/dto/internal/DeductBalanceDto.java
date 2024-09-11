package com.onebucket.domain.WalletManage.dto.internal;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * <br>package name   : com.onebucket.domain.memberManage.dto.request
 * <br>file name      : RequestDeductBalanceDto
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
@Builder
public class DeductBalanceDto implements BalanceDto {
    private BigDecimal amount;
    private String username;

    @Override
    public BigDecimal amount() {
        return amount;
    }

    @Override
    public String username() {
        return username;
    }
}
