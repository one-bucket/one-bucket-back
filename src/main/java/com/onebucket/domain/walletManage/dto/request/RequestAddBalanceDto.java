package com.onebucket.domain.walletManage.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * <br>package name   : com.onebucket.domain.WalletManage.dto.request
 * <br>file name      : RequestAddBalanceDto
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
public record RequestAddBalanceDto(
        @NotNull(message = "Amount cannot be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
        BigDecimal amount
) {
}
