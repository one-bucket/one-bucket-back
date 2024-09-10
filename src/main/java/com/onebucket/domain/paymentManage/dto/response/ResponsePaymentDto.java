package com.onebucket.domain.paymentManage.dto.response;

import com.onebucket.domain.boardManage.entity.post.MarketPost;
import com.onebucket.domain.paymentManage.domain.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.paymentManage.dto.response
 * <br>file name      : ResponsePaymentDto
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
public record ResponsePaymentDto(
        String buyerName,
        String postName,
        String itemName,
        BigDecimal amount,
        LocalDateTime postCreatedAt
) {
    public static ResponsePaymentDto of(MarketPost marketPost, Payment payment) {
        return new ResponsePaymentDto(payment.getBuyer().getNickname(), marketPost.getTitle(), marketPost.getItem(), payment.getAmount(),payment.getCreatedAt());
    }
}
