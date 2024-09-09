package com.onebucket.domain.paymentManage.domain;

import com.onebucket.domain.boardManage.entity.post.MarketPost;
import com.onebucket.domain.memberManage.domain.Member;
import com.onebucket.domain.paymentManage.dto.internal.CreatePaymentDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <br>package name   : com.onebucket.domain.paymentManage.domain
 * <br>file name      : Payment
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
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private Member buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_post_id")  // 외래 키 설정
    private MarketPost marketPost;  // 어떤 판매글에 대한 결제인지

    @Column(scale = 2)  // 소수점 이하 두 자리까지 허용
    private BigDecimal amount;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder(access = AccessLevel.PRIVATE)
    public Payment(Member buyer, MarketPost marketPost, BigDecimal amount) {
        this.buyer = buyer;
        this.marketPost = marketPost;
        this.amount = amount;
    }

    public static Payment create(Member member, MarketPost marketPost, CreatePaymentDto dto) {
        return Payment.builder()
                .buyer(member)
                .marketPost(marketPost)
                .amount(dto.amount())
                .build();
    }
}
