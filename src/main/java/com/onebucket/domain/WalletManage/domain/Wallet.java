package com.onebucket.domain.WalletManage.domain;

import com.onebucket.domain.memberManage.domain.Profile;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * <br>package name   : com.onebucket.domain.memberManage.domain
 * <br>file name      : Wallet
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column(scale = 2)  // 소수점 이하 두 자리까지 허용
    private BigDecimal balance;

    /**
     * 추후 분산락을 도입하여 동시성 문제를 해결하자.
     * @param amount
     */
    public void addBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    /**
     * 추후 분산락을 도입하여 동시성 문제를 해결하자.
     * @param amount
     */
    public void deductBalance(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }
}

