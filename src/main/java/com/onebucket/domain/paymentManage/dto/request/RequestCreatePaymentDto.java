package com.onebucket.domain.paymentManage.dto.request;

import java.math.BigDecimal;

/**
 * <br>package name   : com.onebucket.domain.paymentManage.dto.request
 * <br>file name      : RequestCreatePaymentDto
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
public record RequestCreatePaymentDto(
        Long postId,
        BigDecimal amount
) {
}
