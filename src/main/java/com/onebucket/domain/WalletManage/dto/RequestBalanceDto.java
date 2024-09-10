package com.onebucket.domain.WalletManage.dto;

import java.math.BigDecimal;

/**
 * <br>package name   : com.onebucket.domain.memberManage.dto.request
 * <br>file name      : RequestBalanceDto
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
public interface RequestBalanceDto {
    BigDecimal amount();
    Long profileId();
}
