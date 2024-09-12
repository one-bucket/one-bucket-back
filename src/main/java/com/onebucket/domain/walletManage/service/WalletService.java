package com.onebucket.domain.walletManage.service;

import com.onebucket.domain.walletManage.dto.internal.BalanceDto;

import java.math.BigDecimal;

/**
 * <br>package name   : com.onebucket.domain.memberManage.service
 * <br>file name      : WalletService
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
public interface WalletService {
    void createInitWallet(Long id);

    BigDecimal addBalance(BalanceDto dto);
    BigDecimal deductBalance(BalanceDto dto);

    BigDecimal getBalance(String username);
}
