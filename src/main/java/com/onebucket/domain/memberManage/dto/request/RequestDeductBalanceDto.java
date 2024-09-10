package com.onebucket.domain.memberManage.dto.request;

import lombok.Getter;

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
public class RequestDeductBalanceDto implements RequestBalanceDto{
    private BigDecimal amount;
    private Long profileId;

    @Override
    public BigDecimal amount() {
        return amount;
    }

    @Override
    public Long profileId() {
        return profileId;
    }
}
