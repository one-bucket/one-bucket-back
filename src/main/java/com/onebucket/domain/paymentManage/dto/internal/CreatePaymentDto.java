package com.onebucket.domain.paymentManage.dto.internal;

import com.onebucket.domain.boardManage.entity.post.MarketPost;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.paymentManage.dto.request.RequestCreatePaymentDto;

import java.math.BigDecimal;

/**
 * <br>package name   : com.onebucket.domain.paymentManage.dto
 * <br>file name      : CreatePaymentDto
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
public record CreatePaymentDto(
    String username,
    Long postId,
    BigDecimal amount
) {
    public static CreatePaymentDto of(String username,RequestCreatePaymentDto dto) {
        return new CreatePaymentDto(username,dto.postId(),dto.amount());
    }
}
