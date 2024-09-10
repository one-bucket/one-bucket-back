package com.onebucket.domain.memberManage.service;

import com.onebucket.domain.memberManage.domain.Wallet;
import com.onebucket.domain.memberManage.dto.request.RequestAddBalanceDto;
import com.onebucket.domain.memberManage.dto.request.RequestBalanceDto;
import com.onebucket.domain.memberManage.dto.request.RequestDeductBalanceDto;

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
    BigDecimal addBalance(RequestBalanceDto dto);
    BigDecimal deductBalance(RequestBalanceDto dto);
}
