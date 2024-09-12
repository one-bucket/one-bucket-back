package com.onebucket.domain.walletManage.domain;

import com.onebucket.domain.memberManage.domain.Profile;
import jakarta.persistence.*;
import lombok.*;

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
public class Wallet {

    // member, profile과 같은 id를 사용함.
    @Id
    @Column(nullable = false, updatable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column(scale = 2)  // 소수점 이하 두 자리까지 허용
    private BigDecimal balance;

    public void addBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void deductBalance(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

    @Builder(access = AccessLevel.PRIVATE)
    public Wallet(Long id, Profile profile, BigDecimal balance) {
        this.id = id;
        this.profile = profile;
        this.balance = balance;
    }

    public static Wallet create(Profile profile) {
        return Wallet.builder().id(profile.getId()).profile(profile).balance(BigDecimal.ZERO).build();
    }
}

